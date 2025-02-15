package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import gr.hua.dit.ds.housingsystem.repositories.AvailabilitySlotRepository;
import org.springframework.web.server.ResponseStatusException;
import gr.hua.dit.ds.housingsystem.services.PropertyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private AppUserRepository appUserRepository;

    @Autowired
    private final PropertyService propertyService;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;


    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }


    @PostMapping
    public ResponseEntity<Property> createOrUpdateProperty(@RequestBody Property property) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long ownerId = userDetails.getId();

        AppUser owner = appUserRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        property.setOwner(owner);
        if (property.getAvailabilitySlots() == null) {
            property.setAvailabilitySlots(new ArrayList<>());
        }
        Property savedProperty = propertyService.saveProperty(property, ownerId);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
    }


    @PostMapping("/{id}/availability-slots")
    public void addAvailabilitySlots(Long propertyId, List<AvailabilitySlot> slots) {
        if (slots == null || slots.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one availability slot is required.");
        }
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found."));
        for (AvailabilitySlot slot : slots) {
            slot.setProperty(property);
            slot.validate();
        }
        availabilitySlotRepository.saveAll(slots);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }


    @PostMapping("/{id}/photos")
    public ResponseEntity<String> addPhotoToProperty(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploadDirectory") String uploadDirectory,
            @RequestParam("prefix") String prefix) {
        try {
            propertyService.addPhotoToProperty(id, file, uploadDirectory, prefix);
            return new ResponseEntity<>("Photo added successfully.", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload photo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<List<Property>> getPropertiesByCategory(@PathVariable PropertyCategory category) {
        List<Property> properties = propertyService.getPropertiesByCategory(category);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }


    @GetMapping("/area")
    public ResponseEntity<List<Property>> getPropertiesByArea(@RequestParam String area) {
        List<Property> properties = propertyService.getPropertiesByArea(area);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }


    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Property>> getPropertiesByOwner(@PathVariable Long ownerId) {
        List<Property> properties = propertyService.getPropertiesByOwner(ownerId);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }


    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveProperty(@PathVariable Long id) {
        propertyService.approveProperty(id);
        return new ResponseEntity<>("Property approved successfully.", HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        try {
            propertyService.deleteProperty(id);
            return new ResponseEntity<>("Property deleted successfully.", HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Property not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchProperties(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minRooms) {

        // Delegate the call to the service layer
        List<Property> properties = propertyService.searchProperties(category, minPrice, maxPrice, location, minRooms);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/searchByAreaAndDate")
    public ResponseEntity<List<Property>> searchByAreaAndDate(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String date) {
        List<Property> properties = propertyService.findAvailableProperties(area, date);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Property>> getAvailableProperties() {
        System.out.println("\n\n\nThe request for Available Properties reached the controller\n\n\n");
        List<Property> properties = propertyService.getAvailableProperties();
        return ResponseEntity.ok(properties);
    }
}