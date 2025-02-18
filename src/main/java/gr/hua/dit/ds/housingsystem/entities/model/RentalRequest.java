package gr.hua.dit.ds.housingsystem.entities.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;

@Entity
@Data
public class RentalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
    @JsonBackReference("user-rental")
    @JoinColumn(name = "tenant_id", nullable = false)
    private AppUser tenant;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
    @JsonBackReference("property-rental")
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;


    public void setTenant(AppUser tenant) {
        this.tenant = tenant;
        if (tenant != null) {
            if (tenant.getRentalRequests() == null) {
                tenant.setRentalRequests(new HashSet<>()); // Ensure the set is initialized
            }
            tenant.getRentalRequests().add(this);
        }
    }

    public void setProperty(Property property) {
        this.property = property;
        if (property != null) {
            if (property.getRentalRequests() == null) {
                property.setRentalRequests(new HashSet<>()); // Ensure initialization
            }
            property.getRentalRequests().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalRequest that = (RentalRequest) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

