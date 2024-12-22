package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.model.Photo;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }


    public void uploadPhoto(Long propertyId, MultipartFile file) throws IOException {
        // Validate file
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid photo file.");
        }

        // Generate a unique filename
        String directory = "uploads/propertyPhotos/";
        String uniqueFileName = propertyId + "-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        String filePath = directory + uniqueFileName;

        // Save file to server
        File dest = new File(filePath);
        dest.getParentFile().mkdirs(); // Create directories if they don't exist
        file.transferTo(dest);

        // Save photo entity
        Photo photo = new Photo();
        photo.setFilePath(filePath);
        Property property = new Property(); // Fetch the property using PropertyRepository
        photo.setProperty(property);
        photoRepository.save(photo);
    }
}
