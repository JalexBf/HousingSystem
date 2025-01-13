package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    @Query("SELECT COUNT(a) > 0 FROM AvailabilitySlot a WHERE a.property.id = :propertyId " +
            "AND (a.startTime < :endTime AND a.endTime > :startTime)")
    boolean existsOverlappingSlot(@Param("propertyId") Long propertyId,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM AvailabilitySlot a WHERE a.property.id = :propertyId")
    List<AvailabilitySlot> findAllByPropertyId(@Param("propertyId") Long propertyId);
}
