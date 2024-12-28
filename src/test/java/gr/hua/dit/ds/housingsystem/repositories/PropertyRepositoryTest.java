package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PropertyRepositoryTest {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    public void testSaveAndFindPropertiesByOwner() {
        // Create Owner with ID proof file paths
        AppUser owner = new AppUser();
        owner.setUsername("propertyowner");
        owner.setPassword("password123");
        owner.setFirstName("Owner");
        owner.setLastName("Name");
        owner.setPhone("0987654321");
        owner.setEmail("owner@example.com");
        owner.setAfm("0987654322");
        owner.setRole(UserRole.OWNER);
        owner.setIdFrontPath("uploads/idProofs/owner-front-id.jpg");
        owner.setIdBackPath("uploads/idProofs/owner-back-id.jpg");
        AppUser savedOwner = appUserRepository.save(owner);

        // Create Property
        Property property = new Property();
        property.setCategory(PropertyCategory.HOUSE);
        property.setArea("Suburb");
        property.setAddress("456 Elm St");
        property.setAtak("0987654321");
        property.setPrice(2500.00);
        property.setSquareMeters(120);
        property.setFloor(0);
        property.setNumberOfRooms(4);
        property.setNumberOfBathrooms(2);
        property.setRenovationYear(2018);
        property.setOwner(savedOwner); // Assign owner

        propertyRepository.save(property);

        // Fetch and Verify Properties by Owner
        List<Property> foundProperties = propertyRepository.findByOwnerId(savedOwner.getId());
        assertThat(foundProperties).hasSize(1);
        assertThat(foundProperties.get(0).getAddress()).isEqualTo("456 Elm St");
    }

    @Test
    public void testFindByCategory() {
        // Create Owner with ID proof file paths
        AppUser owner = new AppUser();
        owner.setUsername("categoryowner");
        owner.setPassword("password123");
        owner.setFirstName("Owner");
        owner.setLastName("Name");
        owner.setPhone("0987654300");
        owner.setEmail("owner@example.com");
        owner.setAfm("0987654300");
        owner.setRole(UserRole.OWNER);
        owner.setIdFrontPath("uploads/idProofs/category-front-id.jpg");
        owner.setIdBackPath("uploads/idProofs/category-back-id.jpg");
        AppUser savedOwner = appUserRepository.save(owner);

        // Create Property
        Property property = new Property();
        property.setCategory(PropertyCategory.APARTMENT);
        property.setArea("Downtown");
        property.setAddress("123 Main St");
        property.setAtak("0987654321");
        property.setPrice(1500.00);
        property.setSquareMeters(75);
        property.setFloor(2);
        property.setNumberOfRooms(2);
        property.setNumberOfBathrooms(1);
        property.setRenovationYear(2020);
        property.setOwner(savedOwner); // Assign owner

        propertyRepository.save(property);

        // Fetch and Verify Properties by Category
        List<Property> foundProperties = propertyRepository.findByCategory(PropertyCategory.APARTMENT);
        assertThat(foundProperties).hasSize(1);
        assertThat(foundProperties.get(0).getArea()).isEqualTo("Downtown");
    }

    @Test
    public void testSavePropertyWithMissingOwner() {
        Property property = new Property();
        property.setCategory(PropertyCategory.APARTMENT);
        property.setArea("Test Area");
        property.setAddress("123 Test St");
        property.setAtak("1234567000");
        property.setPrice(1000.00);
        property.setSquareMeters(50);
        property.setFloor(1);
        property.setNumberOfRooms(2);
        property.setNumberOfBathrooms(1);
        property.setRenovationYear(2020);

        assertThatThrownBy(() -> propertyRepository.save(property))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
