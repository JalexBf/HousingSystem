package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.AvailabilitySlotRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
public class AvailabilitySlotService {
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public AvailabilitySlotService(AvailabilitySlotRepository availabilitySlotRepository) {
        this.availabilitySlotRepository = availabilitySlotRepository;
    }

    public void addAvailabilitySlot(Long propertyId, LocalDateTime startTime, LocalDateTime endTime) {
        if (availabilitySlotRepository.existsOverlappingSlot(propertyId, startTime, endTime)) {
            throw new IllegalArgumentException("Overlapping availability slot exists for this property.");
        }

        AvailabilitySlot slot = new AvailabilitySlot();
        Property property = new Property(); // Fetch the property using PropertyRepository
        slot.setProperty(property);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        availabilitySlotRepository.save(slot);
    }
}

