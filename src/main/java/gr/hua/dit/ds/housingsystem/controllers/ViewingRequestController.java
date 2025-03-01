package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.DTO.AvailabilitySlotDTO;
import gr.hua.dit.ds.housingsystem.DTO.TenantDTO;
import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import gr.hua.dit.ds.housingsystem.DTO.PropertyDTO;
import gr.hua.dit.ds.housingsystem.DTO.ViewingRequestDTO;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.ViewingRequestRepository;
import gr.hua.dit.ds.housingsystem.services.UserDetailsImpl;
import gr.hua.dit.ds.housingsystem.services.ViewingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/viewing-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ViewingRequestController {

    @Autowired
    private ViewingRequestService viewingRequestService;

    @Autowired
    private ViewingRequestRepository viewingRequestRepository;

    @Autowired
    private AppUserRepository appUserRepository;


    @GetMapping
    public ResponseEntity<List<ViewingRequestDTO>> getAllViewingRequests() {
        List<ViewingRequest> viewingRequests = viewingRequestService.getAllViewingRequests();

        List<ViewingRequestDTO> dtos = viewingRequests.stream()
                .map(vr -> new ViewingRequestDTO(
                        vr.getId(),
                        vr.getStatus().name(),
                        vr.getProperty() != null ? new PropertyDTO(vr.getProperty().getId(), vr.getProperty().getAddress(), vr.getProperty().getPrice()) : null,
                        vr.getTenant() != null ? new TenantDTO(vr.getTenant().getId(), vr.getTenant().getUsername()) : null,  // ✅ Added this line
                        vr.getAvailabilitySlot() != null ? new AvailabilitySlotDTO(vr.getAvailabilitySlot().getId(), vr.getAvailabilitySlot().getDayOfWeek(), vr.getAvailabilitySlot().getStartHour(), vr.getAvailabilitySlot().getEndHour()) : null
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/my-requests")
    public ResponseEntity<List<ViewingRequestDTO>> getMyViewingRequests(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long ownerId = userDetails.getId();

        List<ViewingRequest> viewingRequests = viewingRequestService.getViewingRequestsByOwnerId(ownerId);
        List<ViewingRequestDTO> dtos = viewingRequests.stream()
                .map(vr -> new ViewingRequestDTO(
                        vr.getId(),
                        vr.getStatus().name(),
                        vr.getProperty() != null ? new PropertyDTO(vr.getProperty().getId(), vr.getProperty().getAddress(), vr.getProperty().getPrice()) : null,
                        vr.getTenant() != null ? new TenantDTO(vr.getTenant().getId(), vr.getTenant().getUsername()) : null,
                        vr.getAvailabilitySlot() != null ? new AvailabilitySlotDTO(vr.getAvailabilitySlot().getId(), vr.getAvailabilitySlot().getDayOfWeek(), vr.getAvailabilitySlot().getStartHour(), vr.getAvailabilitySlot().getEndHour()) : null
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ViewingRequest> getViewingRequestById(@PathVariable Long id) {
        Optional<ViewingRequest> viewingRequest = viewingRequestService.getViewingRequestById(id);
        return viewingRequest.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ViewingRequest> createViewingRequest(@RequestBody ViewingRequest viewingRequest) {
        ViewingRequest createdRequest = viewingRequestService.createViewingRequest(viewingRequest);
        return ResponseEntity.ok(createdRequest);
    }

    @PutMapping("/{id}/manage")
    public ResponseEntity<?> manageViewingRequest(
            @PathVariable Long id,
            @RequestBody ViewingRequest requestBody,
            Authentication authentication) {

        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long ownerId = userDetails.getId();

        ViewingRequest viewingRequest = viewingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viewing Request not found"));

        if (!viewingRequest.getProperty().getOwner().getId().equals(ownerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: You are not the owner of this property");
        }
        return ResponseEntity.ok(viewingRequest);
    }


    @PostMapping("/{id}/status")
    public ResponseEntity<ViewingRequest> updateViewingRequestStatus(
            @PathVariable Long id, @RequestBody Map<String, String> requestBody, Authentication authentication) {

        String status = requestBody.get("status");  // Extract status from JSON

        System.out.println("🔹 Received PUT request for ID: " + id);
        System.out.println("🔹 Status from request body: " + status);

        if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ViewingRequest viewingRequest = viewingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viewing Request not found"));

        if (!viewingRequest.getProperty().getOwner().getId().equals(userDetails.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase());
            ViewingRequest updatedRequest = viewingRequestService.updateViewingRequest(id, requestStatus);
            return ResponseEntity.ok(updatedRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViewingRequest(@PathVariable Long id) {
        viewingRequestService.deleteViewingRequest(id);
        return ResponseEntity.noContent().build();
    }
}
