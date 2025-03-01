package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.DTO.*;
import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.*;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.AvailabilitySlotRepository;
import gr.hua.dit.ds.housingsystem.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private ViewingRequestService viewingRequestService;

    @Autowired
    private final TenantServiceImpl tenantService;

    @Autowired
    private final AppUserRepository appUserRepository;

    @Autowired
    private final PropertyService propertyService;

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;


    public TenantController(TenantServiceImpl tenantService, AppUserRepository appUserRepository, PropertyService propertyService) {
        this.tenantService = tenantService;
        this.appUserRepository = appUserRepository;
        this.propertyService = propertyService;
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
                        new TenantDTO(   // Fix: Added missing TenantDTO argument
                                viewingRequest.getTenant().getId(),
                                viewingRequest.getTenant().getUsername()
                        ),
                        new AvailabilitySlotDTO(
                                viewingRequest.getAvailabilitySlot().getId(),
                                viewingRequest.getAvailabilitySlot().getDayOfWeek(),
                                viewingRequest.getAvailabilitySlot().getStartHour(),
                                viewingRequest.getAvailabilitySlot().getEndHour()
                        ),
                        viewingRequest.getProperty().getId(),
                        viewingRequest.getTenant().getId(),
                        viewingRequest.getAvailabilitySlot().getId()
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
    public ResponseEntity<String> addViewingRequest(@PathVariable Long tenantId, @RequestBody ViewingRequestDTO request) {
        try {
            if (request.getPropertyId() == null || request.getAvailabilitySlotId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Property ID and Availability Slot ID are required.");
            }

            // Fetch required entities
            AppUser tenant = appUserRepository.findById(tenantId)
                    .orElseThrow(() -> new RuntimeException("Tenant not found"));
            Property property = propertyService.getPropertyById(request.getPropertyId());
            AvailabilitySlot availabilitySlot = availabilitySlotRepository.findById(request.getAvailabilitySlotId())
                    .orElseThrow(() -> new RuntimeException("Availability slot not found"));

            // Construct the ViewingRequest entity
            ViewingRequest viewingRequest = new ViewingRequest();
            viewingRequest.setTenant(tenant);
            viewingRequest.setProperty(property);
            viewingRequest.setAvailabilitySlot(availabilitySlot);
            viewingRequest.setStatus(RequestStatus.PENDING);

            viewingRequestService.createViewingRequest(viewingRequest);

            return ResponseEntity.ok("Viewing request submitted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        }
    }
}