package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.DTO.PropertyDTO;
import gr.hua.dit.ds.housingsystem.DTO.RentalRequestDTO;
import gr.hua.dit.ds.housingsystem.DTO.TenantDTO;
import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Photo;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import gr.hua.dit.ds.housingsystem.services.RentalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rental-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RentalRequestController {

    @Autowired
    private RentalRequestService rentalRequestService;


    // Updated for owner manage requests
    @GetMapping
    public ResponseEntity<List<RentalRequestDTO>> getAllRentalRequests() {
        List<RentalRequest> rentalRequests = rentalRequestService.getAllRentalRequests();

        List<RentalRequestDTO> dtos = rentalRequests.stream()
                .map(rr -> {
                    Property property = rr.getProperty();
                    AppUser tenant = rr.getTenant();

                    return new RentalRequestDTO(
                            rr.getId(),
                            rr.getStatus().name(),
                            property != null ? new PropertyDTO(
                                    property.getId(),
                                    property.getAddress(),
                                    property.getPrice()
                            ) : null,
                            tenant != null ? new TenantDTO(tenant.getId(), tenant.getUsername()) : null
                    );
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<RentalRequest> getRentalRequestById(@PathVariable Long id) {
        Optional<RentalRequest> rentalRequest = rentalRequestService.getRentalRequestById(id);
        return rentalRequest.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RentalRequest> createRentalRequest(@RequestBody RentalRequest rentalRequest) {
        RentalRequest createdRequest = rentalRequestService.createRentalRequest(rentalRequest);
        return ResponseEntity.ok(createdRequest);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RentalRequest> updateRentalRequestStatus(
            @PathVariable Long id, @RequestParam RequestStatus status) {
        RentalRequest updatedRequest = rentalRequestService.updateRentalRequest(id, status);
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRentalRequest(@PathVariable Long id) {
        rentalRequestService.deleteRentalRequest(id);
        return ResponseEntity.noContent().build();
    }
}
