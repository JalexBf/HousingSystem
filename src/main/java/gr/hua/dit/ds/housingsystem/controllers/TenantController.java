package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.DTO.*;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.services.*;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    @Transactional
    @GetMapping("/{tenantId}")
    public ResponseEntity<AppUserDTO> getTenant(@PathVariable Long tenantId) {
        System.out.println("\n\n\nThe request reached the controller\n\n\n");
        AppUser tenant = tenantService.getTenantWithRequests(tenantId);
        if (tenant == null) {
            return ResponseEntity.notFound().build();
        }

        AppUserDTO tenantDTO = mapToDTO(tenant);
        return ResponseEntity.ok(tenantDTO);
    }

    private AppUserDTO mapToDTO(AppUser tenant) {
        AppUserDTO dto = new AppUserDTO();
        dto.setId(tenant.getId());
        dto.setUsername(tenant.getUsername());
        dto.setFirstName(tenant.getFirstName());
        dto.setLastName(tenant.getLastName());
        dto.setEmail(tenant.getEmail());
        dto.setPhone(tenant.getPhone());

        // Initialize collections and create defensive copies
        Set<RentalRequestDTO> rentalRequests = new HashSet<>();
        Set<ViewingRequestDTO> viewingRequests = new HashSet<>();

        if (tenant.getRentalRequests() != null) {
            for (RentalRequest rentalRequest : new HashSet<>(tenant.getRentalRequests())) {
                rentalRequests.add(new RentalRequestDTO(
                        rentalRequest.getId(),
                        rentalRequest.getStatus().name(),
                        new PropertyDTO(
                                rentalRequest.getProperty().getId(),
                                rentalRequest.getProperty().getAddress(),
                                rentalRequest.getProperty().getPrice()
                        ),
                        null
                ));
            }
        }

        if (tenant.getViewingRequests() != null) {
            for (ViewingRequest viewingRequest : new HashSet<>(tenant.getViewingRequests())) {
                viewingRequests.add(new ViewingRequestDTO(
                        viewingRequest.getId(),
                        viewingRequest.getStatus().name(),
                        new PropertyDTO(
                                viewingRequest.getProperty().getId(),
                                viewingRequest.getProperty().getAddress(),
                                viewingRequest.getProperty().getPrice()
                        ),
                        null,  // Tenant is not included when returned in TenantController
                        new AvailabilitySlotDTO(
                                viewingRequest.getAvailabilitySlot().getId(),
                                viewingRequest.getAvailabilitySlot().getDayOfWeek(),
                                viewingRequest.getAvailabilitySlot().getStartHour(),
                                viewingRequest.getAvailabilitySlot().getEndHour()
                        )
                ));
            }
        }

        dto.setRentalRequests(rentalRequests);
        dto.setViewingRequests(viewingRequests);

        return dto;
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

}