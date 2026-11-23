package com.autism_rehablito_app.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.autism_rehablito_app.dto.admin.AdminChildCreateRequest;
import com.autism_rehablito_app.dto.admin.AdminChildResponse;
import com.autism_rehablito_app.dto.admin.AdminChildUpdateRequest;
import com.autism_rehablito_app.dto.admin.AdminParentOptionResponse;
import com.autism_rehablito_app.entity.Branch;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapy;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.repository.BranchRepository;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ChildTherapyRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.service.AdminChildService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.util.Comparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@Service
public class AdminChildServiceImpl implements AdminChildService {

    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;
    private final BranchRepository branchRepository;
    private final ChildTherapyRepository childTherapyRepository;
    
    @Value("${app.upload.child-images-dir:uploads/child-photos}")
    private String childImagesDirectory;

    public AdminChildServiceImpl(
            ChildRepository childRepository,
            ParentRepository parentRepository,
            BranchRepository branchRepository,
            ChildTherapyRepository childTherapyRepository) {

        this.childRepository = childRepository;
        this.parentRepository = parentRepository;
        this.branchRepository = branchRepository;
        this.childTherapyRepository = childTherapyRepository;
    }


@Override
@Transactional
public AdminChildResponse createChild(AdminChildCreateRequest request) {

    List<String> requestedTherapies = request.getTherapyTypes();

    if (requestedTherapies == null || requestedTherapies.isEmpty()) {
        throw new IllegalArgumentException(
                "At least one therapy type is required"
        );
    }

    Set<String> allowedTherapies = Set.of(
            "Occupational Therapy",
            "Speech Therapy"
    );

    if (requestedTherapies.size() > 2
            || !allowedTherapies.containsAll(requestedTherapies)
            || requestedTherapies.stream().distinct().count()
                    != requestedTherapies.size()) {

        throw new IllegalArgumentException(
                "Select Occupational Therapy, Speech Therapy, or both"
        );
    }

    Parent parent = parentRepository.findById(request.getParentId())
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Parent not found with id: " + request.getParentId()
                    )
            );

 // Validate Parent account
    if (!Boolean.TRUE.equals(parent.getActive())) {
        throw new IllegalArgumentException(
                "Parent account is inactive"
        );
    }

    // Validate Parent registered branch
    Branch parentBranch = parent.getBranch();

    if (parentBranch == null) {
        throw new IllegalArgumentException(
                "Parent is not registered with any branch"
        );
    }

    // Fetch selected Child branch
    Branch branch = branchRepository.findById(request.getBranchId())
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Branch not found with id: " + request.getBranchId()
                    )
            );

    // Validate selected branch status
    if (!Boolean.TRUE.equals(branch.getActive())) {
        throw new IllegalArgumentException(
                "Branch is inactive"
        );
    }

    // Validate Parent and Child branch mapping
    if (!parentBranch.getId().equals(branch.getId())) {
        throw new IllegalArgumentException(
                "Selected branch does not match parent's registered branch"
        );
    }
    if (!Boolean.TRUE.equals(branch.getActive())) {
        throw new IllegalArgumentException("Branch is inactive");
    }

    Child child = new Child();
    child.setChildName(request.getChildName().trim());
    child.setDateOfBirth(request.getDateOfBirth());
    child.setGender(request.getGender());
    child.setParent(parent);
    child.setBranch(branch);
    child.setMedicalNotes(request.getMedicalNotes());
    child.setActive(true);

    Child savedChild = childRepository.save(child);

    for (String therapyType : requestedTherapies) {
        ChildTherapy childTherapy = new ChildTherapy();
        childTherapy.setChild(savedChild);
        childTherapy.setTherapyType(therapyType);
        childTherapy.setActive(true);

        childTherapyRepository.save(childTherapy);
    }

    return mapToResponse(savedChild);
}

@Override
@Transactional(readOnly = true)
public List<AdminChildResponse> getAllChildren() {

    return childRepository.findAll()
            .stream()
            .sorted(
                    Comparator.comparing(
                            Child::getCreatedAt,
                            Comparator.nullsLast(Comparator.reverseOrder())
                    )
            )
            .map(this::mapToResponse)
            .toList();
}

@Override
@Transactional(readOnly = true)
public List<AdminChildResponse> getChildrenByBranch(
        Long branchId) {

    branchRepository.findById(branchId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Branch not found with id: " + branchId
                    )
            );

    return childRepository.findByBranchId(branchId)
            .stream()
            .sorted(
                    Comparator.comparing(
                            Child::getCreatedAt,
                            Comparator.nullsLast(Comparator.reverseOrder())
                    )
            )
            .map(this::mapToResponse)
            .toList();
}
    @Override
    @Transactional(readOnly = true)
    public AdminChildResponse getChildById(Long childId) {

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Child not found with id: " + childId
                        )
                );

        return mapToResponse(child);
    }


private AdminChildResponse mapToResponse(Child child) {

    Parent parent = child.getParent();
    Branch branch = child.getBranch();

    AdminChildResponse response = new AdminChildResponse(
            child.getId(),
            child.getChildName(),
            child.getDateOfBirth(),
            child.getGender(),

            parent.getId(),
            parent.getUser().getName(),
            parent.getUser().getEmail(),
            parent.getUser().getMobileNumber(),

            branch.getId(),
            branch.getBranchName(),

            child.getMedicalNotes(),
            child.getActive(),
            child.getCreatedAt(),
            child.getImagePath()
    );

    List<String> therapyTypes = childTherapyRepository
            .findByChildIdAndActiveTrue(child.getId())
            .stream()
            .map(ChildTherapy::getTherapyType)
            .distinct()
            .toList();

    response.setTherapyTypes(therapyTypes);

    return response;
}
    
    @Override
    @Transactional
    public AdminChildResponse updateChild(
            Long childId,
            AdminChildUpdateRequest request) {

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Child not found with id: " + childId
                        )
                );

     // Validate Parent
        Parent parent = parentRepository.findById(request.getParentId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Parent not found with id: "
                                        + request.getParentId()
                        )
                );

        if (!Boolean.TRUE.equals(parent.getActive())) {
            throw new IllegalArgumentException(
                    "Parent account is inactive"
            );
        }

        // Validate Parent registered branch
        Branch parentBranch = parent.getBranch();

        if (parentBranch == null) {
            throw new IllegalArgumentException(
                    "Parent is not registered with any branch"
            );
        }

        // Fetch selected Child branch
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Branch not found with id: "
                                        + request.getBranchId()
                        )
                );

        // Validate Branch status
        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new IllegalArgumentException(
                    "Branch is inactive"
            );
        }

        // Validate Parent and Child branch mapping
        if (!parentBranch.getId().equals(branch.getId())) {
            throw new IllegalArgumentException(
                    "Selected branch does not match parent's registered branch"
            );
        }
        child.setChildName(request.getChildName().trim());
        child.setDateOfBirth(request.getDateOfBirth());
        child.setGender(request.getGender());
        child.setParent(parent);
        child.setBranch(branch);
        child.setMedicalNotes(request.getMedicalNotes());

        Child updatedChild = childRepository.save(child);

        return mapToResponse(updatedChild);
    }
    
    @Override
    @Transactional
    public AdminChildResponse activateChild(Long childId) {

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Child not found with id: " + childId
                        )
                );

        if (!Boolean.TRUE.equals(child.getParent().getActive())) {
            throw new IllegalArgumentException(
                    "Child cannot be activated because parent account is inactive"
            );
        }

        if (!Boolean.TRUE.equals(child.getBranch().getActive())) {
            throw new IllegalArgumentException(
                    "Child cannot be activated because branch is inactive"
            );
        }

        child.setActive(true);

        Child activatedChild = childRepository.save(child);

        return mapToResponse(activatedChild);
    }
    
    @Override
    @Transactional
    public AdminChildResponse deactivateChild(Long childId) {

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Child not found with id: " + childId
                        )
                );

        child.setActive(false);

        Child deactivatedChild = childRepository.save(child);

        return mapToResponse(deactivatedChild);
    }
    

@Override
@Transactional(readOnly = true)
public List<AdminParentOptionResponse> getParentsByBranch(
        Long branchId) {

    // Validate branch
    Branch branch = branchRepository.findById(branchId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Branch not found with id: " + branchId
                    )
            );

    // Check branch active status
    if (!Boolean.TRUE.equals(branch.getActive())) {
        throw new IllegalArgumentException(
                "Branch is inactive"
        );
    }

    // Fetch active parents registered under selected branch
    return parentRepository
            .findByBranchIdAndActiveTrue(branchId)
            .stream()
            .map(parent -> {

                // Check whether parent has at least one active child
                boolean childLinked =
                        childRepository.existsByParentIdAndActiveTrue(
                                parent.getId()
                        );

                return new AdminParentOptionResponse(
                        parent.getId(),
                        parent.getUser().getName(),
                        parent.getUser().getEmail(),
                        parent.getUser().getMobileNumber(),
                        parent.getActive(),
                        branch.getId(),
                        branch.getBranchName(),
                        childLinked
                );
            })
            .toList();
}


@Override
@Transactional
public AdminChildResponse uploadChildImage(
        Long childId,
        MultipartFile image) {

    Child child = childRepository.findById(childId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Child not found with id: " + childId
                    )
            );

    if (image == null || image.isEmpty()) {
        throw new IllegalArgumentException(
                "Please select a child image"
        );
    }

    // Validate image size: maximum 5 MB
    if (image.getSize() > 5 * 1024 * 1024) {
        throw new IllegalArgumentException(
                "Image size must not exceed 5 MB"
        );
    }

    // Validate image content type
    String contentType = image.getContentType();

    if (contentType == null ||
            !Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp"
            ).contains(contentType.toLowerCase(Locale.ROOT))) {

        throw new IllegalArgumentException(
                "Only JPG, PNG and WEBP images are allowed"
        );
    }
    
 // Verify actual image content
    try {
        BufferedImage bufferedImage = ImageIO.read(
                new ByteArrayInputStream(image.getBytes())
        );

        if (bufferedImage == null) {
            throw new IllegalArgumentException(
                    "Invalid image file"
            );
        }

        if (bufferedImage.getWidth() > 5000
                || bufferedImage.getHeight() > 5000) {

            throw new IllegalArgumentException(
                    "Image dimensions must not exceed 5000 x 5000"
            );
        }

    } catch (IOException exception) {
        throw new IllegalArgumentException(
                "Unable to verify image content",
                exception
        );
    }

    String extension;

    switch (contentType.toLowerCase(Locale.ROOT)) {
        case "image/jpeg":
            extension = ".jpg";
            break;
        case "image/png":
            extension = ".png";
            break;
        case "image/webp":
            extension = ".webp";
            break;
        default:
            throw new IllegalArgumentException(
                    "Unsupported image format"
            );
    }

    try {
        Path uploadDirectory = Paths.get(childImagesDirectory)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(uploadDirectory);

        String fileName = UUID.randomUUID() + extension;

        Path destination = uploadDirectory.resolve(fileName)
                .normalize();

        if (!destination.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "Invalid image destination"
            );
        }

        Files.copy(image.getInputStream(), destination);

        // Store relative path in database
        child.setImagePath(
                "child-photo/" + fileName
        );

        Child updatedChild = childRepository.save(child);

        return mapToResponse(updatedChild);

    } catch (IOException exception) {

        throw new RuntimeException(
                "Unable to upload child image",
                exception
        );
    }
}

@Override
@Transactional(readOnly = true)
public Resource getChildImage(Long childId) {

    Child child = childRepository.findById(childId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Child not found with id: " + childId
                    )
            );

    String imagePath = child.getImagePath();

    if (imagePath == null || imagePath.isBlank()) {
        throw new IllegalArgumentException(
                "Child image not found"
        );
    }

    try {
        Path uploadDirectory = Paths.get(childImagesDirectory)
                .toAbsolutePath()
                .normalize();

        // DB path se sirf filename lena hai
        String fileName = Paths.get(imagePath)
                .getFileName()
                .toString();

        Path imageFile = uploadDirectory.resolve(fileName)
                .normalize();

        // Ensure file stays inside configured upload directory
        if (!imageFile.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "Invalid child image path"
            );
        }

        if (!Files.exists(imageFile)
                || !Files.isRegularFile(imageFile)) {
            throw new IllegalArgumentException(
                    "Child image file not found on server"
            );
        }

        Resource resource = new UrlResource(imageFile.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new IllegalArgumentException(
                    "Child image is not readable"
            );
        }

        return resource;

    } catch (IOException exception) {
        throw new RuntimeException(
                "Unable to read child image",
                exception
        );
    }
}


}