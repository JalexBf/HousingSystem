package gr.hua.dit.ds.housingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.services.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PropertyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(propertyController).build();
        objectMapper = new ObjectMapper();
    }





    @Test
    void testAddPhotoToProperty() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "photo content".getBytes()
        );

        doNothing().when(propertyService).addPhotoToProperty(anyLong(), any(), anyString(), anyString());

        mockMvc.perform(multipart("/api/properties/1/photos")
                        .file(file)
                        .param("uploadDirectory", "uploads/")
                        .param("prefix", "property"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Photo added successfully."));
    }

    @Test
    void testGetPropertiesByCategory() throws Exception {
        Property property1 = new Property();
        property1.setId(1L);
        property1.setArea("Athens");
        property1.setCategory(PropertyCategory.APARTMENT);

        Property property2 = new Property();
        property2.setId(2L);
        property2.setArea("Thessaloniki");
        property2.setCategory(PropertyCategory.APARTMENT);

        List<Property> properties = Arrays.asList(property1, property2);

        when(propertyService.getPropertiesByCategory(PropertyCategory.APARTMENT)).thenReturn(properties);

        mockMvc.perform(get("/api/properties/category/APARTMENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].area").value("Athens"))
                .andExpect(jsonPath("$[1].area").value("Thessaloniki"));
    }


    @Test
    void testApproveProperty() throws Exception {
        doNothing().when(propertyService).approveProperty(1L);

        mockMvc.perform(post("/api/properties/1/approve"))
                .andExpect(status().isOk())
                .andExpect(content().string("Property approved successfully."));
    }
}
