package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.interfaces.PropertyService;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.AvailabilitySlotRepository;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;

    @Override
    public Property createProperty(Property property, Long ownerId) {
        // Ensure the owner exists and is an OWNER
        AppUser owner = appUserRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found."));
        if (!owner.getRole().equals(UserRole.OWNER)) {
            throw new IllegalArgumentException("User is not authorized as an owner.");
        }

        property.setOwner(owner);
        return propertyRepository.save(property);
    }

    @Override
    public Property updateProperty(Long propertyId, Property updatedProperty) {
        // Ensure the property exists
        Property existingProperty = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found."));

        // Update fields
        existingProperty.setCategory(updatedProperty.getCategory());
        existingProperty.setArea(updatedProperty.getArea());
        existingProperty.setAddress(updatedProperty.getAddress());
        existingProperty.setAtak(updatedProperty.getAtak());
        existingProperty.setPrice(updatedProperty.getPrice());
        existingProperty.setSquareMeters(updatedProperty.getSquareMeters());
        existingProperty.setFloor(updatedProperty.getFloor());
        existingProperty.setNumberOfRooms(updatedProperty.getNumberOfRooms());
        existingProperty.setNumberOfBathrooms(updatedProperty.getNumberOfBathrooms());
        existingProperty.setRenovationYear(updatedProperty.getRenovationYear());
        existingProperty.setAmenities(updatedProperty.getAmenities());

        return propertyRepository.save(existingProperty);
    }

    @Override
    public void deleteProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found."));
        propertyRepository.delete(property);
    }

    @Override
    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found."));
    }

    @Override
    public List<Property> getPropertiesByOwner(Long ownerId) {
        return propertyRepository.findByOwnerId(ownerId);
    }

    @Override
    public void setViewingAvailability(Long propertyId, List<AvailabilitySlot> availabilitySlots) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found."));

        // Update availability slots
        property.getAvailabilitySlots().clear();
        for (AvailabilitySlot slot : availabilitySlots) {
            slot.setProperty(property);
        }
        property.getAvailabilitySlots().addAll(availabilitySlots);

        propertyRepository.save(property);
    }

    @Override
    public List<AvailabilitySlot> getViewingAvailability(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found."));
        return property.getAvailabilitySlots();
    }

    @Override
    public List<Property> searchProperties(String category, String area, Double minPrice, Double maxPrice) {
        return propertyRepository.findAll().stream()
                .filter(property -> (category == null || property.getCategory().toString().equalsIgnoreCase(category)) &&
                        (area == null || property.getArea().toLowerCase().contains(area.toLowerCase())) &&
                        (minPrice == null || property.getPrice() >= minPrice) &&
                        (maxPrice == null || property.getPrice() <= maxPrice))
                .toList();
    }
}
