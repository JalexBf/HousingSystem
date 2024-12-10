package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.AvailabilitySlotRepository;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ViewingAvailabilityTest {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;

    @BeforeEach
    public void cleanUp() {
        availabilitySlotRepository.deleteAll();
        propertyRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    public void testViewingAvailability() {
        // Step 1: Create and save an Owner
        AppUser owner = new AppUser();
        owner.setAfm("1234567800");
        owner.setEmail("owner@example.com");
        owner.setFirstName("Test");
        owner.setLastName("Owner");
        owner.setPhone("1234567890");
        owner.setRole(UserRole.OWNER);
        owner.setPassword("password123");
        owner.setUsername("testowner");
        owner.setIdProof("test-id-proof".getBytes());
        AppUser savedOwner = appUserRepository.save(owner);

        // Step 2: Create and save a Property
        Property property = new Property();
        property.setCategory(PropertyCategory.APARTMENT);
        property.setAddress("Test Address");
        property.setArea("Town");
        property.setAtak("1230000000"); // Unique ATAK
        property.setPrice(1500.00);
        property.setSquareMeters(85);
        property.setFloor(2);
        property.setNumberOfRooms(3);
        property.setNumberOfBathrooms(2);
        property.setRenovationYear(2020);
        property.setOwner(savedOwner); // Link to the owner
        Property savedProperty = propertyRepository.save(property);

        // Step 3: Create and save Availability Slots
        AvailabilitySlot slot1 = new AvailabilitySlot();
        slot1.setStartTime(LocalDateTime.of(2024, 11, 25, 10, 0));
        slot1.setEndTime(LocalDateTime.of(2024, 11, 25, 12, 0));
        slot1.setProperty(savedProperty);

        AvailabilitySlot slot2 = new AvailabilitySlot();
        slot2.setStartTime(LocalDateTime.of(2024, 11, 26, 14, 0));
        slot2.setEndTime(LocalDateTime.of(2024, 11, 26, 16, 0));
        slot2.setProperty(savedProperty);

        availabilitySlotRepository.saveAll(List.of(slot1, slot2));

        // Step 4: Verify slots are saved and associated with the property
        List<AvailabilitySlot> slots = availabilitySlotRepository.findByPropertyId(savedProperty.getId());
        assertThat(slots).hasSize(2);
        assertThat(slots.get(0).getStartTime()).isEqualTo(LocalDateTime.of(2024, 11, 25, 10, 0));
        assertThat(slots.get(1).getStartTime()).isEqualTo(LocalDateTime.of(2024, 11, 26, 14, 0));
    }
}
