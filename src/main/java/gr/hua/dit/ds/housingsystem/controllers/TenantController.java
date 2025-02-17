package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.services.TenantServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {
    TenantServiceImpl tenantService;

    AppUserRepository appUserRepository;

    public TenantController(TenantServiceImpl tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping("")
    public List<AppUser> findAllTenants(){
        return tenantService.findAllTenants();
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<AppUser> getTenant(@PathVariable Long tenantId) {
        Optional<AppUser> appUser = tenantService.getTenant(tenantId);
        System.out.println("\n\n\nThe request reached the controller\n\n\n");
        return appUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{tenantId}")
    public ResponseEntity<AppUser> updateTenant(@PathVariable Long tenantId, @RequestBody AppUser updatedTenant) {
        System.out.println("\nThe update tenant request reached the controller\n");
        AppUser tenant = tenantService.updateTenant(tenantId, updatedTenant);
        return ResponseEntity.ok(tenant);
    }

    @PostMapping("/{tenantId}/add-rental-request")
    public ResponseEntity<AppUser> addRentalRequest(@PathVariable Long tenantId, @RequestBody RentalRequest rentalRequest) {
        System.out.println("\nThe add rental tenant request reached the controller\n");
        AppUser updatedTenant = tenantService.addRentalRequest(tenantId, rentalRequest);
        return ResponseEntity.ok(updatedTenant);
    }

    @PostMapping("/{tenantId}/add-viewing-request")
    public ResponseEntity<AppUser> addViewingRequest(@PathVariable Long tenantId, @RequestBody ViewingRequest viewingRequest) {
        System.out.println("\nThe add viewing tenant request reached the controller\n");
        AppUser updatedTenant = tenantService.addViewingRequest(tenantId, viewingRequest);
        return ResponseEntity.ok(updatedTenant);
    }

//    public Optional<AppUser> getTenant(@PathVariable Long tenantId) {
//        return appUserRepository.findByIdWithRequests(tenantId)
//                .filter(user -> user.getRole() == UserRole.TENANT);
//    }


}
