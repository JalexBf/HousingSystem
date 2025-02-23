package gr.hua.dit.ds.housingsystem.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.tika.Tika;

@Service
public class PhotoService {
    // Define the upload directory relative to the project root
    private final Path uploadDir = Paths.get("uploads", "property-photos");

    public PhotoService() {
        // Create upload directory if it doesn't exist
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    public String uploadPhoto(MultipartFile file, String prefix) throws IOException {
        validatePhoto(file);

        // Generate unique filename
        String fileName = prefix + "-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path targetLocation = uploadDir.resolve(fileName);

        // Save the file
        Files.copy(file.getInputStream(), targetLocation);

        // Return the relative path that will be stored in the database
        return "/property-photos/" + fileName;
    }

    public Resource loadPhotoAsResource(String fileName) throws IOException {
        try {
            Path filePath = uploadDir.resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new IOException("File not found: " + fileName);
            }
        } catch (IOException e) {
            throw new IOException("File not found: " + fileName);
        }
    }

    private void validatePhoto(MultipartFile file) throws IOException {
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image file.");
        }
    }
}