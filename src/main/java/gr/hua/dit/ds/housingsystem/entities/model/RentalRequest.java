package gr.hua.dit.ds.housingsystem.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RentalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
//    @JsonIgnore
    @JoinColumn(name = "tenant_id", nullable = false)
    private AppUser tenant;

    @ManyToOne
//    @JsonIgnore
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;
}