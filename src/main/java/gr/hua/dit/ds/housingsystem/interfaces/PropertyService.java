package gr.hua.dit.ds.housingsystem.interfaces;

import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import java.util.List;


public interface PropertyService {
    Property createProperty(Property property, Long ownerId);

    Property updateProperty(Long propertyId, Property updatedProperty);

    void deleteProperty(Long propertyId);

    Property getPropertyById(Long propertyId);

    List<Property> getPropertiesByOwner(Long ownerId);

    void setViewingAvailability(Long propertyId, List<AvailabilitySlot> availabilitySlots);

    List<AvailabilitySlot> getViewingAvailability(Long propertyId);

    List<Property> searchProperties(String category, String area, Double minPrice, Double maxPrice);
}


