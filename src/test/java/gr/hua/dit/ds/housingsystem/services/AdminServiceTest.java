package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Transactional
public class AdminServiceTest {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    public void testApproveProperty() {
        // Create an owner
        AppUser owner = new AppUser();
        owner.setUsername("owner");
        owner.setPassword("password123");
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setPhone("1234567890");
        owner.setEmail("owner@example.com");
        owner.setAfm("1234567890");
        owner.setRole(UserRole.OWNER);
        AppUser savedOwner = appUserRepository.save(owner);

        // Create a property
        Property property = new Property();
        property.setCategory(PropertyCategory.APARTMENT);
        property.setArea("Downtown");
        property.setAddress("123 Main St");
        property.setAtak("1234567890");
        property.setPrice(1000.00);
        property.setSquareMeters(80);
        property.setFloor(2);
        property.setNumberOfRooms(3);
        property.setNumberOfBathrooms(1);
        property.setRenovationYear(2020);
        property.setApproved(false); // Initially not approved
        property.setOwner(savedOwner); // Assign the owner
        Property savedProperty = propertyRepository.save(property);

        // Approve the property
        adminService.approveProperty(savedProperty.getId());

        // Assert that the property is approved
        Property updatedProperty = propertyRepository.findById(savedProperty.getId())
                .orElseThrow(() -> new IllegalStateException("Property not found"));
        assertThat(updatedProperty.isApproved()).isTrue();
    }
}

