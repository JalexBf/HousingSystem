package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import gr.hua.dit.ds.housingsystem.services.RentalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rental-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RentalRequestController {

    @Autowired
    private RentalRequestService rentalRequestService;

    @GetMapping
    public ResponseEntity<List<RentalRequest>> getAllRentalRequests() {
        List<RentalRequest> rentalRequests = rentalRequestService.getAllRentalRequests();
        return ResponseEntity.ok(rentalRequests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalRequest> getRentalRequestById(@PathVariable Long id) {
        Optional<RentalRequest> rentalRequest = rentalRequestService.getRentalRequestById(id);
        return rentalRequest.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RentalRequest> createRentalRequest(@RequestBody RentalRequest rentalRequest) {
        System.out.println("\n\nRental request received: \n" + rentalRequest);
        RentalRequest createdRequest = rentalRequestService.createRentalRequest(rentalRequest);
        return ResponseEntity.ok(createdRequest);
    }

//    @PostMapping
//    public ResponseEntity<RentalRequest> createRentalRequest(@RequestBody Map<String, Long> requestData) {
//        Long propertyId = requestData.get("propertyId");
//        Long tenantId = requestData.get("tenantId");
//
//        RentalRequest rentalRequest = new RentalRequest();
//        rentalRequest.setProperty(new Property(propertyId)); // Assuming Property has a constructor accepting ID
//        rentalRequest.setTenant(new AppUser(tenantId)); // Assuming AppUser has a constructor accepting ID
//        rentalRequest.setStatus(RequestStatus.PENDING);
//
//        RentalRequest createdRequest = rentalRequestService.createRentalRequest(rentalRequest);
//        return ResponseEntity.ok(createdRequest);
//    }


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