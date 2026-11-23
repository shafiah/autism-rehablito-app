package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.admin.AdminTherapistCreateRequest;
import com.autism_rehablito_app.dto.admin.AdminTherapistCreateResponse;
import com.autism_rehablito_app.dto.admin.AdminTherapistResponse;
import com.autism_rehablito_app.entity.Branch;
import com.autism_rehablito_app.entity.Role;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.repository.BranchRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.UserRepository;
import com.autism_rehablito_app.service.AdminTherapistService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminTherapistServiceImpl
        implements AdminTherapistService {

    private final UserRepository userRepository;
    private final TherapistRepository therapistRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminTherapistServiceImpl(
            UserRepository userRepository,
            TherapistRepository therapistRepository,
            BranchRepository branchRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.therapistRepository = therapistRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AdminTherapistCreateResponse createTherapist(
            AdminTherapistCreateRequest request) {

        String email = request.getEmail().trim();

        String mobileNumber =
                request.getMobileNumber().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        if (userRepository.existsByMobileNumber(mobileNumber)) {
            throw new IllegalArgumentException(
                    "Mobile number is already registered"
            );
        }

        Branch branch = branchRepository
                .findById(request.getBranchId())
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Branch not found with id: "
                                        + request.getBranchId()
                        )
                );

        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new IllegalArgumentException(
                    "Selected branch is inactive"
            );
        }

        User user = new User();

        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setMobileNumber(mobileNumber);

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(Role.THERAPIST);
        user.setActive(true);

        User savedUser =
                userRepository.save(user);

        Therapist therapist =
                new Therapist();

        therapist.setUser(savedUser);
        therapist.setBranch(branch);

        therapist.setTherapyType(
                request.getTherapyType().trim()
        );

        therapist.setAvailability(
                request.getAvailability()
        );

        therapist.setActive(true);

        Therapist savedTherapist =
                therapistRepository.save(therapist);

        return new AdminTherapistCreateResponse(
                savedUser.getId(),
                savedTherapist.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getMobileNumber(),
                savedUser.getRole().name(),
                branch.getId(),
                branch.getBranchName(),
                savedTherapist.getTherapyType(),
                savedTherapist.getAvailability(),
                savedTherapist.getActive(),
                "Therapist created successfully"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTherapistResponse> getAllTherapists() {

        return therapistRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTherapistResponse> getTherapistsByBranch(
            Long branchId) {

        if (!branchRepository.existsById(branchId)) {
            throw new IllegalArgumentException(
                    "Branch not found with id: " + branchId
            );
        }

        return therapistRepository
                .findByBranchId(branchId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminTherapistResponse getTherapistById(
            Long therapistId) {

        Therapist therapist =
                therapistRepository
                        .findById(therapistId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Therapist not found with id: "
                                                + therapistId
                                )
                        );

        return mapToResponse(therapist);
    }

    private AdminTherapistResponse mapToResponse(
            Therapist therapist) {

        User user = therapist.getUser();
        Branch branch = therapist.getBranch();

        return new AdminTherapistResponse(
                user.getId(),
                therapist.getId(),
                user.getName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getRole().name(),
                branch.getId(),
                branch.getBranchName(),
                therapist.getTherapyType(),
                therapist.getAvailability(),
                therapist.getActive(),
                therapist.getCreatedAt()
        );
    }
    
    @Override
    @Transactional
    public AdminTherapistResponse activateTherapist(Long therapistId) {

        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Therapist not found with id: " + therapistId
                        )
                );

        User user = therapist.getUser();

        therapist.setActive(true);
        user.setActive(true);

        therapistRepository.save(therapist);
        userRepository.save(user);

        return mapToResponse(therapist);
    }
    
    @Override
    @Transactional
    public AdminTherapistResponse deactivateTherapist(Long therapistId) {

        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Therapist not found with id: " + therapistId
                        )
                );

        User user = therapist.getUser();

        therapist.setActive(false);
        user.setActive(false);

        therapistRepository.save(therapist);
        userRepository.save(user);

        return mapToResponse(therapist);
    }
}