package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.DTO.AvailabilitySlotDTO;
import gr.hua.dit.ds.housingsystem.DTO.TenantDTO;
import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import gr.hua.dit.ds.housingsystem.DTO.PropertyDTO;
import gr.hua.dit.ds.housingsystem.DTO.ViewingRequestDTO;
import gr.hua.dit.ds.housingsystem.services.ViewingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/viewing-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ViewingRequestController {

    @Autowired
    private ViewingRequestService viewingRequestService;

    // Updated for owner manage requests
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

    @PutMapping("/{id}/status")
    public ResponseEntity<ViewingRequest> updateViewingRequestStatus(
            @PathVariable Long id, @RequestParam RequestStatus status) {
        ViewingRequest updatedRequest = viewingRequestService.updateViewingRequest(id, status);
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViewingRequest(@PathVariable Long id) {
        viewingRequestService.deleteViewingRequest(id);
        return ResponseEntity.noContent().build();
    }
}
