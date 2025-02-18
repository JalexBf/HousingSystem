package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.AvailabilitySlotRepository;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import gr.hua.dit.ds.housingsystem.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;


    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createOrUpdateProperty(
            @RequestPart("property") Property property,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        // Validate ATAK before checking authentication
        if (property.getAtak() == null || property.getAtak().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ATAK number is required.");
        }
        if (propertyRepository.existsByAtak(property.getAtak())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ATAK number is already in use.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
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

        if (property.getAvailabilitySlots() != null && !property.getAvailabilitySlots().isEmpty()) {
            propertyService.addAvailabilitySlots(savedProperty.getId(), property.getAvailabilitySlots());
        }

        System.out.println("Files received: " + (files != null ? files.size() : "null"));
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                System.out.println("Uploading file: " + file.getOriginalFilename());
                try {
                    propertyService.addPhotoToProperty(savedProperty.getId(), file, "property_photos", "property");
                } catch (IOException e) {
                    System.out.println("Error saving photo: " + e.getMessage());
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to save photos: " + e.getMessage());
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProperty);
    }


    @PostMapping("/{id}/availability-slots")
    public void addAvailabilitySlots(@PathVariable Long propertyId, @RequestBody List<AvailabilitySlot> slots) {
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
    public ResponseEntity<PropertyDTO> getPropertyById(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        if (property == null) {
            return ResponseEntity.notFound().build();
        }
        List<AvailabilitySlotDTO> availabilitySlots = property.getAvailabilitySlots() != null
                ? property.getAvailabilitySlots().stream()
                .map(slot -> new AvailabilitySlotDTO(
                        slot.getId(),
                        slot.getDayOfWeek().name(),
                        slot.getStartHour(),
                        slot.getEndHour()))
                .collect(Collectors.toList())
                : new ArrayList<>();

        PropertyDTO propertyDTO = new PropertyDTO(
                property.getId(),
                property.getCategory().name(),
                property.getArea(),
                property.getAddress(),
                property.getPrice(),
                property.getSquareMeters(),
                property.getFloor(),
                property.getNumberOfRooms(),
                property.getNumberOfBathrooms(),
                property.getRenovationYear(),
                property.getAtak(),
                property.getAmenities() != null
                        ? property.getAmenities().stream().map(Enum::name).collect(Collectors.toList())
                        : new ArrayList<>(),
                property.getPhotos() != null
                        ? property.getPhotos().stream()
                        .map(photo -> "/images/" + Paths.get(photo.getFilePath()).getFileName().toString())
                        .collect(Collectors.toList())
                        : new ArrayList<>(),
                availabilitySlots
        );
        return ResponseEntity.ok(propertyDTO);
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


    @DeleteMapping("/api/properties/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId(); // Get logged-in user's ID

        propertyService.deleteProperty(id, userId);

        return ResponseEntity.ok("Property deleted successfully");
    }


    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getProperties() {
        List<Property> properties = propertyService.getAllProperties();

        List<PropertyDTO> propertyDTOs = properties.stream()
                .map(property -> new PropertyDTO(
                        property.getId(),
                        property.getCategory().name(),
                        property.getArea(),
                        property.getAddress(),
                        property.getPrice(),
                        property.getSquareMeters(),
                        property.getFloor(),
                        property.getNumberOfRooms(),
                        property.getNumberOfBathrooms(),
                        property.getRenovationYear(),
                        property.getAtak(),
                        property.getAmenities().stream()
                                .map(Enum::name)
                                .collect(Collectors.toList()),
                        property.getPhotos().stream()
                                .map(photo -> "/images/" + Paths.get(photo.getFilePath()).getFileName().toString())
                                .collect(Collectors.toList()),
                        property.getAvailabilitySlots().stream()
                                .map(slot -> new AvailabilitySlotDTO(
                                        slot.getId(),
                                        slot.getDayOfWeek().name(),
                                        slot.getStartHour(),
                                        slot.getEndHour()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(propertyDTOs);
    }


    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path path = Paths.get(System.getProperty("user.home") + "/property_photos/" + filename);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Detect MIME type dynamically
            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default if unknown
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mimeType)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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


    @GetMapping("/search")
    public ResponseEntity<List<PropertyDTO>> searchProperties(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minRooms,
            @RequestParam(required = false) Integer maxRooms,
            @RequestParam(required = false) Integer minSquareMeters,
            @RequestParam(required = false) Integer maxSquareMeters) {

        List<Property> properties = propertyService.searchProperties(area, category, minPrice, maxPrice, minRooms, maxRooms, minSquareMeters, maxSquareMeters);

        List<PropertyDTO> propertyDTOs = properties.stream().map(property -> new PropertyDTO(
                property.getId(),
                property.getCategory().name(),
                property.getArea(),
                property.getAddress(),
                property.getPrice(),
                property.getSquareMeters(),
                property.getFloor(),
                property.getNumberOfRooms(),
                property.getNumberOfBathrooms(),
                property.getRenovationYear(),
                property.getAtak(),
                property.getAmenities().stream().map(Enum::name).collect(Collectors.toList()),
                property.getPhotos().stream()
                        .map(photo -> "/images/" + Paths.get(photo.getFilePath()).getFileName().toString())
                        .collect(Collectors.toList()),
                property.getAvailabilitySlots().stream()
                        .map(slot -> new AvailabilitySlotDTO(
                                slot.getId(),
                                slot.getDayOfWeek().name(),
                                slot.getStartHour(),
                                slot.getEndHour()))
                        .collect(Collectors.toList())  // Add this collect() call
        )).collect(Collectors.toList());

        return ResponseEntity.ok(propertyDTOs);
    }
}
