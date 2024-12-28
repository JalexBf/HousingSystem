package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantServiceImpl {
    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> findAllTenants() {
        return appUserRepository.findAll().stream()
                .filter(user -> user.getRole() == UserRole.TENANT)
                .toList();
    }

    public AppUser saveTenant(AppUser tenant) {
        if (tenant.getRole() != UserRole.TENANT) {
            throw new IllegalArgumentException("User role must be TENANT");
        }
        return appUserRepository.save(tenant);
    }

    public void deleteTenant(Long id) {
        AppUser tenant = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
        if (tenant.getRole() != UserRole.TENANT) {
            throw new IllegalArgumentException("User is not a TENANT");
        }
        appUserRepository.deleteById(id);
    }

    public Optional<AppUser> getTenant(Long id) {
        return appUserRepository.findById(id)
                .filter(user -> user.getRole() == UserRole.TENANT);
    }
}
