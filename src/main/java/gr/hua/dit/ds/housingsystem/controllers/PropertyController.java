package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.services.PropertyService;
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


    // Create or Update Property
    @PostMapping
    public ResponseEntity<Property> saveProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.saveProperty(property);
        return ResponseEntity.ok(savedProperty);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(property);
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<List<Property>> getPropertiesByCategory(@PathVariable PropertyCategory category) {
        List<Property> properties = propertyService.getPropertiesByCategory(category);
        return ResponseEntity.ok(properties);
    }


    @GetMapping("/area")
    public ResponseEntity<List<Property>> getPropertiesByArea(@RequestParam String area) {
        List<Property> properties = propertyService.getPropertiesByArea(area);
        return ResponseEntity.ok(properties);
    }


    // Add Photo to Property
    @PostMapping("/{id}/photos")
    public ResponseEntity<String> addPhotoToProperty(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploadDirectory") String uploadDirectory,
            @RequestParam("prefix") String prefix) throws IOException {
        propertyService.addPhotoToProperty(id, file, uploadDirectory, prefix);
        return ResponseEntity.ok("Photo added successfully");
    }


    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveProperty(@PathVariable Long id) {
        propertyService.approveProperty(id);
        return ResponseEntity.ok("Property approved successfully");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property deleted successfully");
    }
}
