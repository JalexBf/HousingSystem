package gr.hua.dit.ds.housingsystem.entities.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
//    @JsonIgnore
    @JsonBackReference("user-rental")
    @JoinColumn(name = "tenant_id", nullable = false)
    private AppUser tenant;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
//    @JsonIgnore
    @JsonBackReference("property-rental")
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    public void setTenant(AppUser tenant) {
        this.tenant = tenant;
        if (tenant != null && !tenant.getRental().contains(this)) {
            tenant.getRental().add(this);
        }
    }
}