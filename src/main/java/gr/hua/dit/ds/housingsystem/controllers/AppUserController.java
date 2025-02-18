package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.services.AppUserService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/{userId}/idProof")
    public ResponseEntity<String> uploadIdImages(
            @PathVariable Long userId,
            @RequestParam("idFront") MultipartFile idFront,
            @RequestParam("idBack") MultipartFile idBack) {
        try {
            appUserService.uploadIdImages(userId, idFront, idBack);
            return ResponseEntity.ok("ID images uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload ID images.");
        }
    }

    @GetMapping("/{userId}/idProof/front")
    public ResponseEntity<Resource> getIdFront(@PathVariable Long userId) {
        AppUser user = appUserService.getUserById(userId);
        return serveFile(user.getIdFrontPath());
    }

    @GetMapping("/{userId}/idProof/back")
    public ResponseEntity<Resource> getIdBack(@PathVariable Long userId) {
        AppUser user = appUserService.getUserById(userId);
        return serveFile(user.getIdBackPath());
    }

    private ResponseEntity<Resource> serveFile(String filePath) {
        FileSystemResource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
