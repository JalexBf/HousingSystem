package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    public void testSaveAndFindUser() {
        byte[] idProof = "sample-id-proof".getBytes(); // Placeholder binary data

        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setEmail("johndoe@example.com");
        user.setAfm("1234567890");
        user.setRole(UserRole.TENANT);
        user.setIdProof(idProof); // Ensure idProof is set

        AppUser savedUser = appUserRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();

        AppUser foundUser = appUserRepository.findByUsername("testuser").orElse(null);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("johndoe@example.com");
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
    public void testSaveDuplicateUser() {
        byte[] idProof = "duplicate-user-proof".getBytes();

        AppUser user1 = new AppUser();
        user1.setUsername("duplicateuser");
        user1.setPassword("password123");
        user1.setFirstName("User");
        user1.setLastName("One");
        user1.setEmail("duplicate@example.com");
        user1.setPhone("1234567890");
        user1.setAfm("1234567890");
        user1.setRole(UserRole.TENANT);
        user1.setIdProof(idProof);
        appUserRepository.save(user1);

        AppUser user2 = new AppUser();
        user2.setUsername("duplicateuser");
        user2.setPassword("password123");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setEmail("duplicate@example.com");
        user2.setPhone("0987654321");
        user2.setAfm("0987654321");
        user2.setRole(UserRole.TENANT);
        user2.setIdProof(idProof);

        assertThatThrownBy(() -> appUserRepository.save(user2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    public void testFetchNonExistentUser() {
        assertThatThrownBy(() -> appUserRepository.findById(9999L)
                .orElseThrow(() -> new EntityNotFoundException("User not found")))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found");
    }


}
