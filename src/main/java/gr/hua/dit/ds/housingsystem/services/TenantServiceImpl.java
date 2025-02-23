package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
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

    public AppUser addRentalRequest(Long tenantId, RentalRequest rentalRequest) {
        AppUser tenant = appUserRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Property property = rentalRequest.getProperty();
        if (property == null) {
            throw new RuntimeException("Rental request must be linked to a property");
        }

        rentalRequest.setTenant(tenant); // Assign tenant
        property.addRentalRequest(rentalRequest); // Ensure it's added to the property's list

        tenant.addRentalRequest(rentalRequest); // Ensure it's added to the tenant's list
        appUserRepository.save(tenant); // Save tenant (cascade should handle property update)
        return tenant;
    }

    public AppUser addViewingRequest(Long tenantId, ViewingRequest viewingRequest) {
        AppUser tenant = appUserRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Property property = viewingRequest.getProperty();
        if (property == null) {
            throw new RuntimeException("Viewing request must be linked to a property");
        }

        viewingRequest.setTenant(tenant); // Assign tenant
        property.addViewingRequest(viewingRequest); // Ensure it's added to the property's list

        tenant.addViewingRequest(viewingRequest); // Ensure it's added to the tenant's list
        appUserRepository.save(tenant); // Save tenant (cascade should handle property update)
        return tenant;
    }


    @Transactional
    public AppUser getTenantWithRequests(Long tenantId) {
        AppUser tenant = appUserRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));


        System.out.println("Initializing rental and viewing collections");
        Hibernate.initialize(tenant.getRentalRequests());
        Hibernate.initialize(tenant.getViewingRequests());

        return tenant;
    }
}
