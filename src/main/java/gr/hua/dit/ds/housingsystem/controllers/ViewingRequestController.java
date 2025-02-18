package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.ViewingRequestRepository;
import gr.hua.dit.ds.housingsystem.services.ViewingRequestService;
import gr.hua.dit.ds.housingsystem.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/viewing-requests")
public class ViewingRequestController {

    @Autowired
    private ViewingRequestService viewingRequestService;

    @Autowired
    private ViewingRequestRepository viewingRequestRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping
    public ResponseEntity<List<ViewingRequest>> getAllViewingRequests() {
        List<ViewingRequest> viewingRequests = viewingRequestService.getAllViewingRequests();
        return ResponseEntity.ok(viewingRequests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViewingRequest> getViewingRequestById(@PathVariable Long id) {
        Optional<ViewingRequest> viewingRequest = viewingRequestService.getViewingRequestById(id);
        return viewingRequest.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ViewingRequest> createViewingRequest(@RequestBody ViewingRequest viewingRequest) {
        ViewingRequest createdRequest = viewingRequestService.createViewingRequest(viewingRequest);
        return ResponseEntity.ok(createdRequest);
    }

    // ✅ Updated to follow PropertyController authentication logic
    @PutMapping("/{id}/manage")
    public ResponseEntity<?> manageViewingRequest(
            @PathVariable Long id,
            @RequestBody ViewingRequest requestBody,  // 🔹 Accept JSON request body
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

        viewingRequest.setStatus(requestBody.getStatus()); // ✅ Read status from JSON body
        viewingRequestRepository.save(viewingRequest);

        return ResponseEntity.ok(viewingRequest);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViewingRequest(@PathVariable Long id) {
        viewingRequestService.deleteViewingRequest(id);
        return ResponseEntity.noContent().build();
    }
}
