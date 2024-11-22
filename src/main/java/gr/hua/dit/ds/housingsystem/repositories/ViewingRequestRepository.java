package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewingRequestRepository extends JpaRepository<ViewingRequest, Long> {
    // Find viewing requests for a specific property
    List<ViewingRequest> findByPropertyId(Long propertyId);

    // Find viewing requests by tenant ID
    List<ViewingRequest> findByTenantId(Long tenantId);

    // Find viewing requests by status
    List<ViewingRequest> findByStatus(RequestStatus status);
}

