package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.services.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    // Upload Photo (Generic)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploadDirectory") String uploadDirectory,
            @RequestParam("prefix") String prefix) {
        try {
            String filePath = photoService.uploadPhoto(file, uploadDirectory, prefix);
            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
