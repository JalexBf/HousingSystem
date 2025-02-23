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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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


    @Transactional
    public Property saveProperty(Property property, Long ownerId) {
        AppUser owner = appUserRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        property.setOwner(owner);

        // Ensure slots are properly linked
        if (property.getAvailabilitySlots() != null && !property.getAvailabilitySlots().isEmpty()) {
            for (AvailabilitySlot slot : property.getAvailabilitySlots()) {
                slot.setProperty(property); // 🔥 Ensure the slot is linked to the property
            }
        }

        return propertyRepository.save(property); // 🔥 Hibernate will now persist slots due to CascadeType.ALL
    }





    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));
    }


    @Transactional
    public void addPhotoToProperty(Long propertyId, MultipartFile file, String uploadDirectory, String prefix) throws IOException {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));

        Path uploadPath = Paths.get("uploads/property-photos/");
        Files.createDirectories(uploadPath);

        String fileName = prefix + "_" + System.currentTimeMillis() + "_" + property.getId() +
                "_" + file.getOriginalFilename();
        Path destinationFile = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        Photo photo = new Photo();
        photo.setFilePath("/property-photos/" + fileName);
        photo.setProperty(property);
        photoRepository.save(photo);

        if (property.getPhotos() == null) {
            property.setPhotos(new HashSet<>());
        }
        property.getPhotos().add(photo);
        propertyRepository.save(property);
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

    public List<Property> getAvailableProperties() {
        return propertyRepository.findAllAvailableProperties();
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }

}
