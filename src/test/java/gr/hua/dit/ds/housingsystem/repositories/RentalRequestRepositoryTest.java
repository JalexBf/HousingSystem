package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RentalRequestRepositoryTest {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    public void testSaveAndFindRentalRequests() {
        byte[] ownerIdProof = "owner-id-proof".getBytes();
        byte[] tenantIdProof = "tenant-id-proof".getBytes();

        // Create Owner
        AppUser owner = new AppUser();
        owner.setUsername("rentalowner");
        owner.setPassword("password123");
        owner.setFirstName("Rental");
        owner.setLastName("Owner");
        owner.setPhone("1234567893");
        owner.setEmail("rentalowner@example.com");
        owner.setAfm("1234567893");
        owner.setRole(UserRole.OWNER);
        owner.setIdProof(ownerIdProof);
        AppUser savedOwner = appUserRepository.save(owner);

        // Create Tenant
        AppUser tenant = new AppUser();
        tenant.setUsername("rentaltenant");
        tenant.setPassword("password123");
        tenant.setFirstName("Rental");
        tenant.setLastName("Tenant");
        tenant.setPhone("1234567894");
        tenant.setEmail("rentaltenant@example.com");
        tenant.setAfm("1234567894");
        tenant.setRole(UserRole.TENANT);
        tenant.setIdProof(tenantIdProof);
        AppUser savedTenant = appUserRepository.save(tenant);

        // Create Property
        Property property = new Property();
        property.setCategory(PropertyCategory.APARTMENT);
        property.setArea("Rental Area");
        property.setAddress("321 Rental St");
        property.setAtak("3213213210");
        property.setPrice(2000.00);
        property.setSquareMeters(100);
        property.setFloor(3);
        property.setNumberOfRooms(3);
        property.setNumberOfBathrooms(2);
        property.setRenovationYear(2020);
        property.setOwner(savedOwner);
        Property savedProperty = propertyRepository.save(property);

        // Create Rental Request
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setProperty(savedProperty);
        rentalRequest.setTenant(savedTenant);
        rentalRequest.setStatus(RequestStatus.PENDING);
        rentalRequestRepository.save(rentalRequest);

        // Fetch and Assert by Tenant
        List<RentalRequest> foundRequests = rentalRequestRepository.findByTenantId(savedTenant.getId());
        assertThat(foundRequests).hasSize(1);
        assertThat(foundRequests.get(0).getStatus()).isEqualTo(RequestStatus.PENDING);

        // Fetch and Assert by Property
        List<RentalRequest> propertyRequests = rentalRequestRepository.findByPropertyId(savedProperty.getId());
        assertThat(propertyRequests).hasSize(1);
        assertThat(propertyRequests.get(0).getTenant().getUsername()).isEqualTo("rentaltenant");
    }



}

