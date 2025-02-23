package gr.hua.dit.ds.housingsystem.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.tika.Tika;

@Service
public class PhotoService {

    public String uploadPhoto(MultipartFile file, String uploadDirectory, String prefix) throws IOException {
        validatePhoto(file); // Apply validation
        String fileName = prefix + "-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        String filePath = uploadDirectory + fileName;

        File dest = new File(filePath);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);

        return filePath;
    }



    private void validatePhoto(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("The photo file is empty.");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("The uploaded file is not an image.");
        }

        Tika tika = new Tika();
        String mimeType = tika.detect(file.getBytes());
        if (!mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image format.");
        }
    }
}
