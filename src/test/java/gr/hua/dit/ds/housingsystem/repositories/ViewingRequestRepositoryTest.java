package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ViewingRequestRepositoryTest {

    @Autowired
    private ViewingRequestRepository viewingRequestRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    public void testSaveAndFindByStatus() {
        byte[] idProof = "tenant-id-proof".getBytes(); // Placeholder binary data

        // Create Tenant with a unique AFM
        AppUser tenant = new AppUser();
        tenant.setUsername("testtenant");
        tenant.setPassword("password123");
        tenant.setFirstName("Test");
        tenant.setLastName("Tenant");
        tenant.setPhone("1234567890");
        tenant.setEmail("tenant@example.com");
        tenant.setAfm("1234567891"); // Unique AFM
        tenant.setRole(UserRole.TENANT);
        tenant.setIdProof(idProof);
        AppUser savedTenant = appUserRepository.save(tenant);

        byte[] ownerIdProof = "owner-id-proof".getBytes();

        // Create Owner with a unique AFM
        AppUser owner = new AppUser();
        owner.setUsername("viewingowner");
        owner.setPassword("password123");
        owner.setFirstName("Owner");
        owner.setLastName("Name");
        owner.setPhone("1234567891");
        owner.setEmail("owner@example.com");
        owner.setAfm("1234567892"); // Unique AFM
        owner.setRole(UserRole.OWNER);
        owner.setIdProof(ownerIdProof);
        AppUser savedOwner = appUserRepository.save(owner);

        // Create Property owned by the unique owner
        Property property = new Property();
        property.setCategory(PropertyCategory.APARTMENT);
        property.setArea("City Center");
        property.setAddress("789 Oak St");
        property.setAtak("1122334455");
        property.setPrice(1800.00);
        property.setSquareMeters(90);
        property.setFloor(2);
        property.setNumberOfRooms(3);
        property.setNumberOfBathrooms(2);
        property.setRenovationYear(2021);
        property.setOwner(savedOwner); // Assign the owner
        propertyRepository.save(property);

        // Create Viewing Request
        ViewingRequest viewingRequest = new ViewingRequest();
        viewingRequest.setProperty(property);
        viewingRequest.setTenant(savedTenant); // Set tenant
        viewingRequest.setProposedTime(LocalDateTime.now().plusDays(3));
        viewingRequest.setStatus(RequestStatus.PENDING);

        viewingRequestRepository.save(viewingRequest);

        // Fetch and Assert
        List<ViewingRequest> foundRequests = viewingRequestRepository.findByStatus(RequestStatus.PENDING);
        assertThat(foundRequests).hasSize(1);
        assertThat(foundRequests.get(0).getProperty().getAddress()).isEqualTo("789 Oak St");
    }

}
