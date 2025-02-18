package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import gr.hua.dit.ds.housingsystem.entities.model.Photo;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.AvailabilitySlotRepository;
import gr.hua.dit.ds.housingsystem.repositories.PhotoRepository;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PhotoService photoService;
    private final PhotoRepository photoRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final AppUserRepository appUserRepository;

    public PropertyService(PropertyRepository propertyRepository, PhotoService photoService,
                           PhotoRepository photoRepository, AvailabilitySlotRepository availabilitySlotRepository, AppUserRepository appUserRepository) {
        this.propertyRepository = propertyRepository;
        this.photoService = photoService;
        this.photoRepository = photoRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.appUserRepository = appUserRepository;
    }


    public Property saveProperty(Property property, Long ownerId) {
        AppUser owner = appUserRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        property.setOwner(owner);

        if (property.getAvailabilitySlots() != null) {
            property.getAvailabilitySlots().forEach(slot -> slot.setProperty(property));
        }

        return propertyRepository.save(property);
    }


    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with ID: " + propertyId));
    }


    @Transactional
    public void addPhotoToProperty(Long propertyId, MultipartFile file, String uploadDirectory, String prefix) throws IOException {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));

        // Create upload directory in user's home
        String userHome = System.getProperty("user.home");
        String fullUploadPath = userHome + File.separator + "property_photos";
        File directory = new File(fullUploadPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + fullUploadPath);
            }
        }

        String fileName = prefix + "_" + System.currentTimeMillis() + "_" + property.getId() +
                "_" + file.getOriginalFilename();
        File destinationFile = new File(directory, fileName);

        // Save the file
        file.transferTo(destinationFile);

        // Create and save the photo entity
        Photo photo = new Photo();
        photo.setFilePath(destinationFile.getAbsolutePath());
        photo.setProperty(property);
        photoRepository.save(photo);

        // Add photo to property's photos collection
        if (property.getPhotos() == null) {
            property.setPhotos(new HashSet<>());
        }
        property.getPhotos().add(photo);
        propertyRepository.save(property);
    }


    public void addAvailabilitySlots(Long propertyId, List<AvailabilitySlot> slots) {
        if (slots == null || slots.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one availability slot is required.");
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found."));

        for (AvailabilitySlot slot : slots) {
            validateAvailabilitySlot(slot);
            slot.setProperty(property);
        }

        availabilitySlotRepository.saveAll(slots);
    }


    private void validateAvailabilitySlot(AvailabilitySlot slot) {
        if (slot.getDayOfWeek() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day of the week is required.");
        }
        if (slot.getStartHour() < 6 || slot.getStartHour() >= 22 || slot.getEndHour() <= slot.getStartHour() || slot.getEndHour() > 22) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid time slot. Hours must be between 06:00 and 22:00.");
        }

        // Ensure no overlapping slots exist for the same property
        List<AvailabilitySlot> existingSlots = availabilitySlotRepository.findByPropertyIdAndDayOfWeek(
                slot.getProperty().getId(), slot.getDayOfWeek());

        for (AvailabilitySlot existingSlot : existingSlots) {
            if (!Objects.equals(existingSlot.getId(), slot.getId()) &&
                    (slot.getStartHour() < existingSlot.getEndHour() && slot.getEndHour() > existingSlot.getStartHour())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Conflicting availability slot detected on " + slot.getDayOfWeek());
            }
        }
    }


    public List<Property> getPropertiesByCategory(PropertyCategory category) {
        return propertyRepository.findByCategory(category);
    }


    public List<Property> getPropertiesByArea(String area) {
        return propertyRepository.findByAreaContaining(area);
    }


    public List<Property> getPropertiesByOwner(Long ownerId) {
        List<Property> properties = propertyRepository.findByOwnerId(ownerId);
        return properties != null ? properties : new ArrayList<>();
    }


    public void approveProperty(Long propertyId) {
        Property property = getPropertyById(propertyId);
        property.setApproved(true);
        propertyRepository.save(property);
    }


    public void deleteProperty(Long propertyId, Long userId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        if (!property.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own properties");
        }
        propertyRepository.delete(property);
    }


    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }


    public List<Property> searchProperties(String area, String category, Integer minPrice, Integer maxPrice,
                                           Integer minRooms, Integer maxRooms, Integer minSquareMeters, Integer maxSquareMeters) {
        List<Property> properties = propertyRepository.findAll(); // Fetch all properties

        return properties.stream()
                .filter(property -> area == null || property.getArea().equalsIgnoreCase(area))
                .filter(property -> category == null || property.getCategory().name().equalsIgnoreCase(category))
                .filter(property -> minPrice == null || property.getPrice() >= minPrice)
                .filter(property -> maxPrice == null || property.getPrice() <= maxPrice)
                .filter(property -> minRooms == null || property.getNumberOfRooms() >= minRooms)
                .filter(property -> maxRooms == null || property.getNumberOfRooms() <= maxRooms)
                .filter(property -> minSquareMeters == null || property.getSquareMeters() >= minSquareMeters)
                .filter(property -> maxSquareMeters == null || property.getSquareMeters() <= maxSquareMeters)
                .collect(Collectors.toList());
    }


    public List<Property> findAvailableProperties(String area, String date) {
        LocalDateTime parsedDate = null;
        if (date != null && !date.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            parsedDate = LocalDateTime.parse(date + " 00:00", formatter);
        }
        return propertyRepository.findAvailableProperties(area, parsedDate);
    }

    @Transactional
    public List<PropertyDTO> getAvailableProperties() {
        List<Property> properties = propertyRepository.findByApproved(true);
        return properties.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PropertyDTO mapToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getId());
        dto.setAddress(property.getAddress());
        dto.setPrice(property.getPrice());
        // Map other fields
        return dto;
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }



}