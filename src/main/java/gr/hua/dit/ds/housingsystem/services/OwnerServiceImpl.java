package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class OwnerServiceImpl {

    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> findAllOwners() {
        return appUserRepository.findAll().stream()
                .filter(user -> user.getRole() == UserRole.OWNER)
                .toList();
    }

    public AppUser saveOwner(AppUser owner) {
        if (owner.getRole() != UserRole.OWNER) {
            throw new IllegalArgumentException("User role must be OWNER");
        }
        return appUserRepository.save(owner);
    }

    public void deleteOwner(Long id) {
        AppUser owner = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        if (owner.getRole() != UserRole.OWNER) {
            throw new IllegalArgumentException("User is not an OWNER");
        }
        appUserRepository.deleteById(id);
    }

    public Optional<AppUser> getOwner(Long id) {
        return appUserRepository.findById(id)
                .filter(user -> user.getRole() == UserRole.OWNER);
    }
}