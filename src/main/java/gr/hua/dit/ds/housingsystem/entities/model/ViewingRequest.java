package gr.hua.dit.ds.housingsystem.entities.model;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ViewingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private AppUser tenant;

    private LocalDateTime proposedTime;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING; // Status: PENDING, APPROVED, REJECTED, CANCELED
}
