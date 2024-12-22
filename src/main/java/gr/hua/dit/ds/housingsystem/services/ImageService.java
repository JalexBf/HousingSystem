package gr.hua.dit.ds.housingsystem.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.apache.tika.Tika;


@Service
public class ImageService {

    public byte[] uploadImage(MultipartFile file) throws IOException {
        // Validate file
        if (file.isEmpty() || !isValidImage(file)) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Convert file to byte array
        return file.getBytes();
    }


    public ResponseEntity<byte[]> serveImage(byte[] imageData, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }


    public boolean isValidImage(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getBytes());
        return mimeType.startsWith("image/");
    }

}

