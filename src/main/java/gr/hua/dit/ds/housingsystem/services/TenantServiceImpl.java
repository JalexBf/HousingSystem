package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
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

    public AppUser updateTenant(Long id, AppUser updatedTenant) {
        return appUserRepository.findById(id)
                .map(tenant -> {
                    // Update only the allowed fields
                    tenant.setUsername(updatedTenant.getUsername());
                    tenant.setFirstName(updatedTenant.getFirstName());
                    tenant.setLastName(updatedTenant.getLastName());
                    tenant.setPhone(updatedTenant.getPhone());
                    tenant.setEmail(updatedTenant.getEmail());
                    return appUserRepository.save(tenant);
                })
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
        }

    public AppUser addRentalRequest(Long tenantId, RentalRequest rentalRequest) {
        AppUser tenant = appUserRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        rentalRequest.setTenant(tenant); // Set the tenant for the request
        tenant.addRentalRequest(rentalRequest); // Add the request to the tenant's set
        return appUserRepository.save(tenant); // Save the updated tenant
    }

    public AppUser addViewingRequest(Long tenantId, ViewingRequest viewingRequest) {
        AppUser tenant = appUserRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        viewingRequest.setTenant(tenant); // Set the tenant for the request
        tenant.getViewing().add(viewingRequest); // Add the request to the tenant's set
        return appUserRepository.save(tenant); // Save the updated tenant
    }
}
