package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Photo;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AppUserRepository appUserRepository;



    @Test
    public void testSaveAndFindPhotosByProperty() {
        // Create Owner
        AppUser owner = new AppUser();
        owner.setUsername("photoowner");
        owner.setPassword("password123");
        owner.setFirstName("Photo");
        owner.setLastName("Owner");
        owner.setPhone("1234567895");
        owner.setEmail("photoowner@example.com");
        owner.setAfm("1234567899");
        owner.setRole(UserRole.OWNER);
        owner.setIdFrontPath("uploads/idProofs/owner-front-id.jpg");
        owner.setIdBackPath("uploads/idProofs/owner-back-id.jpg");
        AppUser savedOwner = appUserRepository.save(owner);

        // Create Property
        Property property = new Property();
        property.setCategory(PropertyCategory.DETACHED_HOUSE);
        property.setArea("Photo Area");
        property.setAddress("123 Photo St");
        property.setAtak("1231231230");
        property.setPrice(3000.00);
        property.setSquareMeters(150);
        property.setFloor(1);
        property.setNumberOfRooms(4);
        property.setNumberOfBathrooms(3);
        property.setRenovationYear(2019);
        property.setOwner(savedOwner);
        Property savedProperty = propertyRepository.save(property);

        // Create Photos
        Photo photo1 = new Photo();
        photo1.setFilePath("uploads/propertyPhotos/property-photo-1.jpg");
        photo1.setProperty(savedProperty);
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setFilePath("uploads/propertyPhotos/property-photo-2.jpg");
        photo2.setProperty(savedProperty);
        photoRepository.save(photo2);

        // Fetch and Assert Photos
        List<Photo> foundPhotos = photoRepository.findByPropertyId(savedProperty.getId());
        assertThat(foundPhotos).hasSize(2);
        assertThat(foundPhotos.get(0).getFilePath()).isEqualTo("uploads/propertyPhotos/property-photo-1.jpg");
        assertThat(foundPhotos.get(1).getFilePath()).isEqualTo("uploads/propertyPhotos/property-photo-2.jpg");
    }
}
