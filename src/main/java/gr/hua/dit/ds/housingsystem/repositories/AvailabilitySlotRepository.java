package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByPropertyId(Long propertyId);
}

