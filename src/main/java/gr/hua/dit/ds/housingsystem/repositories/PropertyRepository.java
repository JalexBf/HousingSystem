package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Find properties by category
    List<Property> findByCategory(PropertyCategory category);

    // Find properties by area containing a specific string
    List<Property> findByAreaContaining(String area);

    // Find properties owned by a specific user
    List<Property> findByOwnerId(Long ownerId);

    // Retrieve properties pending approval
    List<Property> findByApproved(boolean approved);

}
