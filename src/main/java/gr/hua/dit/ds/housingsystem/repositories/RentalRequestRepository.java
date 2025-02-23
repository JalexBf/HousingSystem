package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

    List<RentalRequest> findByPropertyId(Long propertyId);

    List<RentalRequest> findByTenantId(Long tenantId);

    List<RentalRequest> findByStatus(RequestStatus status);

    Page<RentalRequest> findByStatus(RequestStatus status, Pageable pageable);

    @Query("SELECT rr FROM RentalRequest rr " +
            "LEFT JOIN FETCH rr.tenant " +
            "LEFT JOIN FETCH rr.property")
    List<RentalRequest> findAllWithDetails();


}

