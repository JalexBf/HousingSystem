package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {
    // Find rental requests for a specific property
    List<RentalRequest> findByPropertyId(Long propertyId);

    // Find rental requests by tenant ID
    List<RentalRequest> findByTenantId(Long tenantId);

    // Find rental requests by status
    List<RentalRequest> findByStatus(RequestStatus status);

    Page<RentalRequest> findByStatus(RequestStatus status, Pageable pageable);

}

