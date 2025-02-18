package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class AdminService {

    private final AppUserRepository appUserRepository;
    private final PropertyRepository propertyRepository;

    public AdminService(AppUserRepository appUserRepository, PropertyRepository propertyRepository) {
        this.appUserRepository = appUserRepository;
        this.propertyRepository = propertyRepository;
    }


    public void approveProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));
        property.setApproved(true);
        propertyRepository.save(property);
    }

    public void rejectProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));
        propertyRepository.delete(property);
    }


    public List<Property> getUnapprovedProperties() {
        return propertyRepository.findByApproved(false);
    }


    public void approveUser(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setApproved(true);
        appUserRepository.save(user);
    }


    public void rejectUser(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setApproved(false); // Maintain record instead of deletion
        appUserRepository.save(user);
    }


    public void deleteUser(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        appUserRepository.delete(user);
    }


    public void updateUser(Long userId, AppUser updatedUser) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (updatedUser.getIdFrontPath() == null) {
            updatedUser.setIdFrontPath(user.getIdFrontPath());
        }
        if (updatedUser.getIdBackPath() == null) {
            updatedUser.setIdBackPath(user.getIdBackPath());
        }

        if (!isUsernameUnique(updatedUser.getUsername(), userId)) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        if (!isEmailUnique(updatedUser.getEmail(), userId)) {
            throw new IllegalArgumentException("Email is already taken.");
        }

        if (!updatedUser.getPhone().matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone must be exactly 10 digits.");
        }
        if (!updatedUser.getAfm().matches("\\d{10}")) {
            throw new IllegalArgumentException("AFM must be exactly 10 digits.");
        }

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());
        user.setEmail(updatedUser.getEmail());
        user.setAfm(updatedUser.getAfm());
        user.setIdFrontPath(updatedUser.getIdFrontPath());
        user.setIdBackPath(updatedUser.getIdBackPath());

        appUserRepository.save(user);
    }



    public List<AppUser> getUnapprovedUsers() {
        return appUserRepository.findByApproved(false);
    }


    public List<AppUser> getApprovedUsers() {
        return appUserRepository.findByApproved(true);
    }

    public boolean isUsernameUnique(String username, Long excludeId) {
        Optional<AppUser> optionalUser = appUserRepository.findByUsername(username);

        if (!optionalUser.isPresent()) {
            return true;
        }

        AppUser existing = optionalUser.get();

        if (excludeId != null && existing.getId().equals(excludeId)) {
            return true;
        }

        return false;
    }


    public AppUser getUserById(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));
    }

    public boolean isEmailUnique(String email, Long excludeId) {
        Optional<AppUser> optionalUser = appUserRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {
            return true;
        }

        AppUser existing = optionalUser.get();

        if (excludeId != null && existing.getId().equals(excludeId)) {
            return true;
        }

        return false;
    }

}
