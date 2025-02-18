package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
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

    @GetMapping
    public ResponseEntity<List<ViewingRequest>> getAllViewingRequests() {
        List<ViewingRequest> viewingRequests = viewingRequestService.getAllViewingRequests();
        return ResponseEntity.ok(viewingRequests);
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
