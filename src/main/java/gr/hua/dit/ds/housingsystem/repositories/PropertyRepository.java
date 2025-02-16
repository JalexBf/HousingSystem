package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByCategory(PropertyCategory category);

    List<Property> findByAreaContaining(String area);

    List<Property> findByOwnerId(Long ownerId);

    List<Property> findByApproved(boolean approved);

    @Query("SELECT p FROM Property p WHERE " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:location IS NULL OR p.address LIKE %:location%) AND " +
            "(:minRooms IS NULL OR p.numberOfRooms >= :minRooms)")
    List<Property> searchProperties(@Param("category") String category,
                                    @Param("minPrice") Double minPrice,
                                    @Param("maxPrice") Double maxPrice,
                                    @Param("location") String location,
                                    @Param("minRooms") Integer minRooms);

    @Query("SELECT DISTINCT p FROM Property p " +
            "JOIN p.availabilitySlots a " +
            "WHERE (:area IS NULL OR p.area = :area) ")
    List<Property> findAvailableProperties(
            @Param("area") String area,
            @Param("date") LocalDateTime date);

    @Query("SELECT DISTINCT p FROM Property p WHERE p.approved IS TRUE")
    List<Property> findAllAvailableProperties();

}