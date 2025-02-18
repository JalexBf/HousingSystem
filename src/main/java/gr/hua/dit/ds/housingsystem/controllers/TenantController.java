package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.AvailabilitySlotRepository;
import gr.hua.dit.ds.housingsystem.services.PropertyService;
import gr.hua.dit.ds.housingsystem.services.TenantServiceImpl;
import gr.hua.dit.ds.housingsystem.services.ViewingRequestDTO;
import gr.hua.dit.ds.housingsystem.services.ViewingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private ViewingRequestService viewingRequestService;
    private final TenantServiceImpl tenantService;
    private final AppUserRepository appUserRepository;
    private final PropertyService propertyService;

    public TenantController(TenantServiceImpl tenantService, AppUserRepository appUserRepository, PropertyService propertyService) {
        this.tenantService = tenantService;
        this.appUserRepository = appUserRepository;
        this.propertyService = propertyService;
    }

    @GetMapping("")
    public List<AppUser> findAllTenants() {
        return tenantService.findAllTenants();
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<AppUser> getTenant(@PathVariable Long tenantId) {
        Optional<AppUser> appUser = tenantService.getTenant(tenantId);
        System.out.println("\n\n\nThe request reached the controller\n\n\n");
        return appUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;

    @PostMapping("/{tenantId}/add-viewing-request")
    public ResponseEntity<String> addViewingRequest(@PathVariable Long tenantId, @RequestBody ViewingRequestDTO request) {
        try {
            if (request.getPropertyId() == null || request.getAvailabilitySlotId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Property ID and Availability Slot ID are required.");
            }

            AppUser tenant = appUserRepository.findById(tenantId)
                    .orElseThrow(() -> new RuntimeException("Tenant not found"));

            Property property = propertyService.getPropertyById(request.getPropertyId());
            if (property == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found.");
            }

            AvailabilitySlot availabilitySlot = availabilitySlotRepository.findById(request.getAvailabilitySlotId())
                    .orElseThrow(() -> new RuntimeException("Availability slot not found"));

            ViewingRequest viewingRequest = new ViewingRequest();
            viewingRequest.setTenant(tenant);
            viewingRequest.setProperty(property);
            viewingRequest.setAvailabilitySlot(availabilitySlot);
            viewingRequest.setStatus(RequestStatus.PENDING);

            viewingRequestService.createViewingRequest(viewingRequest);

            return ResponseEntity.ok("Viewing request submitted and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        }
    }
}
