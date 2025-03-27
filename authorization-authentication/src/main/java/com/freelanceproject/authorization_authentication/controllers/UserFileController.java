package com.freelanceproject.authorization_authentication.controllers;

import com.freelanceproject.authorization_authentication.model.UserEntity;
import com.freelanceproject.authorization_authentication.repository.UserRepository;
import com.freelanceproject.authorization_authentication.service.FileStorageService;
import com.freelanceproject.authorization_authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/user")
public class UserFileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/{userId}/cv")
    public ResponseEntity<String> uploadCV(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        try {
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));;

            // Delete old file if exists
            if (user.getCvFilename() != null) {
                fileStorageService.deleteFile(user.getCvFilename());
            }

            // Store new file
            String filename = fileStorageService.storeFile(file);

            // Update user entity
            user.setCvFilename(filename);
            userRepository.save(user);

            return ResponseEntity.ok("CV uploaded successfully: " + filename);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload CV: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/cv")
    public ResponseEntity<Resource> downloadCV(@PathVariable Long userId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getCvFilename() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(uploadDir).resolve(user.getCvFilename()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{userId}/cv")
    public ResponseEntity<String> deleteCV(@PathVariable Long userId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getCvFilename() != null) {
                fileStorageService.deleteFile(user.getCvFilename());
                user.setCvFilename(null);
                userRepository.save(user);
            }

            return ResponseEntity.ok("CV deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete CV: " + e.getMessage());
        }
    }
}
