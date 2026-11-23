package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.parent.ParentChildResponse;
import com.autism_rehablito_app.dto.parent.ParentChildTherapistResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.ParentChildService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/api/parent/children")
public class ParentChildController {

    private final ParentChildService parentChildService;

    public ParentChildController(
            ParentChildService parentChildService) {

        this.parentChildService = parentChildService;
    }
    
    @Value("${app.upload.child-images-dir:/var/www/html/child-photo}")
    private String childImagesDirectory;

    @GetMapping
    public ResponseEntity<List<ParentChildResponse>>
    getMyChildren(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentChildService.getMyChildren(
                        user.getId()
                )
        );
    }

    @GetMapping("/{childId}")
    public ResponseEntity<ParentChildResponse>
    getMyChildById(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentChildService.getMyChildById(
                        user.getId(),
                        childId
                )
        );
    }
    
    @GetMapping("/{childId}/therapists")
    public ResponseEntity<List<ParentChildTherapistResponse>>
    getMyChildTherapists(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentChildService.getMyChildTherapists(
                        user.getId(),
                        childId
                )
        );
    }
    
    @GetMapping("/{childId}/image")
    public ResponseEntity<Resource> getMyChildImage(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        // Existing service verifies parent-child ownership.
        ParentChildResponse child =
                parentChildService.getMyChildById(
                        user.getId(),
                        childId
                );

        if (child.getImagePath() == null
                || child.getImagePath().trim().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Only use the stored filename, not an arbitrary path.
            String fileName = Paths.get(child.getImagePath())
                    .getFileName()
                    .toString();

            Path imagePath = Paths.get(childImagesDirectory)
                    .resolve(fileName)
                    .normalize();

            Path allowedDirectory = Paths.get(childImagesDirectory)
                    .toAbsolutePath()
                    .normalize();

            if (!imagePath.toAbsolutePath().startsWith(allowedDirectory)
                    || !Files.isRegularFile(imagePath)) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(imagePath);

            MediaType mediaType =
                    contentType != null
                            ? MediaType.parseMediaType(contentType)
                            : MediaType.APPLICATION_OCTET_STREAM;

            Resource resource = new FileSystemResource(imagePath);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(
                            HttpHeaders.CACHE_CONTROL,
                            "private, no-store"
                    )
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}