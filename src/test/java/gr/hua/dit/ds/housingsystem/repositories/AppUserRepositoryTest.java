package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.services.AppUserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
@Transactional
public class AppUserRepositoryTest {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    public void cleanUp() {
        appUserRepository.deleteAll(); // Delete entities first
    }


    @Test
    public void testUploadAndRetrieveIdImages() throws IOException {
        // Create and save a user
        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("1234567890");
        user.setEmail("testuser@example.com");
        user.setAfm("1234567890");
        user.setRole(UserRole.TENANT);
        AppUser savedUser = appUserRepository.save(user);

        // Mock ID images
        MockMultipartFile frontFile = new MockMultipartFile(
                "idFront", "front-id.jpg", "image/jpeg", "mock front image data".getBytes());
        MockMultipartFile backFile = new MockMultipartFile(
                "idBack", "back-id.jpg", "image/jpeg", "mock back image data".getBytes());

        // Upload images
        appUserService.uploadIdImages(savedUser.getId(), frontFile, backFile);

        // Verify file paths in the database
        AppUser updatedUser = appUserRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(updatedUser.getIdFrontPath()).isEqualTo("uploads/idProofs/" + savedUser.getId() + "-front-id.jpg");
        assertThat(updatedUser.getIdBackPath()).isEqualTo("uploads/idProofs/" + savedUser.getId() + "-back-id.jpg");
    }




    @Test
    public void testSaveUserWithMissingFields() {
        AppUser user = new AppUser();
        user.setPassword("password123");
        user.setRole(UserRole.TENANT);

        assertThatThrownBy(() -> appUserRepository.save(user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    public void testDuplicateAfmThrowsException() {
        // First user
        AppUser user1 = new AppUser();
        user1.setUsername("user1");
        user1.setPassword("password123");
        user1.setFirstName("User");
        user1.setLastName("One");
        user1.setPhone("1234567891");
        user1.setEmail("user1@example.com");
        user1.setAfm("1234567890");
        user1.setRole(UserRole.TENANT);
        user1.setIdFrontPath("uploads/idProofs/1-front-id.jpg");
        user1.setIdBackPath("uploads/idProofs/1-back-id.jpg");

        appUserRepository.save(user1);

        // Second user with the same AFM
        AppUser user2 = new AppUser();
        user2.setUsername("user2");
        user2.setPassword("password123");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setPhone("1234567892");
        user2.setEmail("user2@example.com");
        user2.setAfm("1234567890"); // Duplicate AFM
        user2.setRole(UserRole.TENANT);
        user2.setIdFrontPath("uploads/idProofs/2-front-id.jpg");
        user2.setIdBackPath("uploads/idProofs/2-back-id.jpg");

        assertThatThrownBy(() -> appUserRepository.save(user2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
