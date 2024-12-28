package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.services.PhotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PhotoController.class)
class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    @Test
    void uploadPhoto_ShouldReturnSuccessMessage() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "Test content".getBytes()
        );

        when(photoService.uploadPhoto(any(MultipartFile.class), eq("uploads/"), eq("test")))
                .thenReturn("uploads/test-file.jpg");

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(mockFile)
                        .param("uploadDirectory", "uploads/")
                        .param("prefix", "test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Photo uploaded successfully to: uploads/test-file.jpg"));

        verify(photoService, times(1)).uploadPhoto(any(MultipartFile.class), eq("uploads/"), eq("test"));
    }
}
