package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @InjectMocks
    private PropertyService propertyService;

    @Mock
    private PropertyRepository propertyRepository;

    private Property mockProperty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockProperty = new Property();
        mockProperty.setId(1L);
        mockProperty.setCategory(PropertyCategory.APARTMENT);
        mockProperty.setArea("Downtown");
        mockProperty.setAddress("123 Main Street");
        mockProperty.setAtak("1234567890");
        mockProperty.setPrice(1200.00);
        mockProperty.setSquareMeters(75);
        mockProperty.setApproved(false);
    }

    @Test
    void saveProperty_ShouldSaveProperty() {
        when(propertyRepository.save(any(Property.class))).thenReturn(mockProperty);

        Property savedProperty = propertyService.saveProperty(mockProperty);

        assertNotNull(savedProperty);
        assertEquals(mockProperty.getId(), savedProperty.getId());
        verify(propertyRepository, times(1)).save(mockProperty);
    }

    @Test
    void getPropertyById_ShouldReturnProperty_WhenExists() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(mockProperty));

        Property property = propertyService.getPropertyById(1L);

        assertNotNull(property);
        assertEquals(mockProperty.getId(), property.getId());
        verify(propertyRepository, times(1)).findById(1L);
    }

    @Test
    void getPropertyById_ShouldThrowException_WhenNotExists() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> propertyService.getPropertyById(1L));
        verify(propertyRepository, times(1)).findById(1L);
    }

    @Test
    void getPropertiesByCategory_ShouldReturnList() {
        when(propertyRepository.findByCategory(PropertyCategory.APARTMENT))
                .thenReturn(List.of(mockProperty));

        List<Property> properties = propertyService.getPropertiesByCategory(PropertyCategory.APARTMENT);

        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertEquals(1, properties.size());
        verify(propertyRepository, times(1)).findByCategory(PropertyCategory.APARTMENT);
    }

    @Test
    void approveProperty_ShouldSetApprovedAndSave() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(mockProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(mockProperty);

        propertyService.approveProperty(1L);

        ArgumentCaptor<Property> propertyCaptor = ArgumentCaptor.forClass(Property.class);
        verify(propertyRepository).save(propertyCaptor.capture());
        Property approvedProperty = propertyCaptor.getValue();

        assertTrue(approvedProperty.isApproved());
    }

    @Test
    void deleteProperty_ShouldDeleteProperty_WhenExists() {
        when(propertyRepository.existsById(1L)).thenReturn(true);

        propertyService.deleteProperty(1L);

        verify(propertyRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProperty_ShouldThrowException_WhenNotExists() {
        when(propertyRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> propertyService.deleteProperty(1L));
        verify(propertyRepository, never()).deleteById(1L);
    }
}