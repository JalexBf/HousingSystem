package gr.hua.dit.ds.housingsystem.services;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;


class PhotoServiceTest {

    private final PhotoService photoService = new PhotoService();

    @Test
    void testUploadPhoto() throws IOException {
        // Arrange
        String uploadDirectory = "target/test-uploads/";
        String prefix = "test";
        String testPhotoPath = "src/main/resources/photos/test1.jpg";

        File testPhotoFile = new File(testPhotoPath);
        assertTrue(testPhotoFile.exists(), "Test photo does not exist in the resources directory.");

        FileInputStream fis = new FileInputStream(testPhotoFile);
        MockMultipartFile mockPhoto = new MockMultipartFile(
                "file",
                testPhotoFile.getName(),
                "image/jpeg",
                fis
        );

        // Act
        String uploadedFilePath = photoService.uploadPhoto(mockPhoto, uploadDirectory, prefix);

        // Assert
        File uploadedFile = new File(uploadedFilePath);
        assertTrue(uploadedFile.exists(), "Uploaded file does not exist.");
        assertTrue(uploadedFilePath.contains(uploadDirectory), "File path does not match the upload directory.");
        assertTrue(uploadedFilePath.contains(prefix), "File path does not include the prefix.");

        // Clean up
        uploadedFile.delete();
    }

    @Test
    void testUploadInvalidFile() {
        // Arrange
        String uploadDirectory = "target/test-uploads/";
        String prefix = "test";
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "This is a test file.".getBytes()
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> photoService.uploadPhoto(invalidFile, uploadDirectory, prefix)
        );
        assertEquals("The uploaded file is not an image.", exception.getMessage());
    }
}