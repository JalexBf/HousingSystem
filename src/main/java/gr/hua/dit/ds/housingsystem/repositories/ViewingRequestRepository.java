package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ViewingRequestRepository extends JpaRepository<ViewingRequest, Long> {

    List<ViewingRequest> findByPropertyId(Long propertyId);

    List<ViewingRequest> findByTenantId(Long tenantId);

    List<ViewingRequest> findByPropertyOwnerId(Long ownerId);

    List<ViewingRequest> findByStatus(RequestStatus status);

    @Query("SELECT vr FROM ViewingRequest vr " +
            "LEFT JOIN FETCH vr.tenant " +
            "LEFT JOIN FETCH vr.property " +
            "LEFT JOIN FETCH vr.availabilitySlot")
    List<ViewingRequest> findAllWithDetails();
}

