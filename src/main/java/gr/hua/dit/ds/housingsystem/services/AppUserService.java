package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;


@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }


    public AppUser createUser(AppUser appUser) {
        validateAfm(appUser.getAfm());
        return appUserRepository.save(appUser);
    }


    public void uploadIdImages(Long userId, MultipartFile idFront, MultipartFile idBack) throws IOException {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Validate files
        if (idFront.isEmpty() || !idFront.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid front ID file");
        }
        if (idBack.isEmpty() || !idBack.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid back ID file");
        }

        // Save files to the server
        String frontPath = saveFile(idFront, userId, "front");
        String backPath = saveFile(idBack, userId, "back");

        // Update user entity with file paths
        user.setIdFrontPath(frontPath);
        user.setIdBackPath(backPath);
        appUserRepository.save(user);
    }


    private String saveFile(MultipartFile file, Long userId, String type) throws IOException {
        String directory = "uploads/idProofs/";
        String filePath = directory + userId + "-" + type + "-" + file.getOriginalFilename();
        File dest = new File(filePath);
        dest.getParentFile().mkdirs(); // Create directories if not exist
        file.transferTo(dest);
        return filePath;
    }


    public AppUser getUserById(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }


    public void validateAfm(String afm) {
        if (!afm.matches("\\d{10}")) {
            throw new IllegalArgumentException("AFM must be exactly 10 digits.");
        }
        // Add checksum validation logic here if needed
    }



}
