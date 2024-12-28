package gr.hua.dit.ds.housingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.services.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PropertyController.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mock dependencies of the controller
    private PropertyService propertyService;

    private Property mockProperty;

    @BeforeEach
    void setUp() {
        mockProperty = new Property();
        mockProperty.setId(1L);
        mockProperty.setCategory(PropertyCategory.APARTMENT);
        mockProperty.setArea("Downtown");
        mockProperty.setAddress("123 Main Street");
        mockProperty.setAtak("1234567890");
        mockProperty.setPrice(1200.0);
        mockProperty.setSquareMeters(75);
        mockProperty.setApproved(false);
    }


    @Test
    void saveProperty_ShouldReturnSavedProperty() throws Exception {
        when(propertyService.saveProperty(any(Property.class))).thenReturn(mockProperty);

        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockProperty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(propertyService, times(1)).saveProperty(any(Property.class));
    }


    @Test
    void getPropertyById_ShouldReturnProperty() throws Exception {
        when(propertyService.getPropertyById(1L)).thenReturn(mockProperty);

        mockMvc.perform(get("/api/properties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(propertyService, times(1)).getPropertyById(1L);
    }
}

