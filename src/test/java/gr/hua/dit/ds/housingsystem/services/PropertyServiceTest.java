package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApproveProperty() {
        // Create a property
        Property property = new Property();
        property.setId(1L);
        property.setApproved(false);

        // Mock repository behavior
        when(propertyRepository.findById(1L)).thenReturn(java.util.Optional.of(property));

        // Approve the property
        adminService.approveProperty(1L);

        // Verify the property is approved
        assertThat(property.isApproved()).isTrue();
        verify(propertyRepository).save(property);
    }
}
