package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // Find photos by property ID
    List<Photo> findByPropertyId(Long propertyId);
}
