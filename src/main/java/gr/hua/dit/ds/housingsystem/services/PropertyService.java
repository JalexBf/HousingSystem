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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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


    public void addPhotoToProperty(Long propertyId, MultipartFile file, String uploadDirectory, String prefix) throws IOException {
        Property property = getPropertyById(propertyId);
        String filePath = photoService.uploadPhoto(file, uploadDirectory, prefix);

        Photo photo = new Photo();
        photo.setFilePath(filePath);
        photo.setProperty(property);

        photoRepository.save(photo);
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
        return propertyRepository.findByOwnerId(ownerId);
    }


    public void approveProperty(Long propertyId) {
        Property property = getPropertyById(propertyId);
        property.setApproved(true);
        propertyRepository.save(property);
    }


    public void deleteProperty(Long propertyId) {
        if (!propertyRepository.existsById(propertyId)) {
            throw new EntityNotFoundException("Property not found with ID: " + propertyId);
        }
        propertyRepository.deleteById(propertyId);
    }
}