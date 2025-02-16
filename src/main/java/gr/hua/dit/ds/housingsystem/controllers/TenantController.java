package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.services.TenantServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}