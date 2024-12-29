package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.services.PropertyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<Property> createOrUpdateProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.saveProperty(property);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
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
}
