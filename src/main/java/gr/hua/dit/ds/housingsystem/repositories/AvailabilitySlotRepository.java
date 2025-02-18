package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;


@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByPropertyId(Long propertyId);

    List<AvailabilitySlot> findByPropertyIdAndDayOfWeek(Long propertyId, DayOfWeek dayOfWeek);

    Optional<AvailabilitySlot> findById(Long id);

}
