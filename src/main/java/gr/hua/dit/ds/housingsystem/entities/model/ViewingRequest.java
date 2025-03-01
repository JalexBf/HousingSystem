package gr.hua.dit.ds.housingsystem.entities.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Data
public class ViewingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("property-viewing")
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("user-viewing")
    @JoinColumn(name = "tenant_id", nullable = false)
    private AppUser tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availability_slot_id", nullable = false)
    @JsonBackReference("availability-viewing")
    private AvailabilitySlot availabilitySlot;


    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING; // Status: PENDING, APPROVED, REJECTED, CANCELED


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewingRequest that = (ViewingRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public void setTenant(AppUser tenant) {
        this.tenant = tenant;
        if (tenant != null) {
            if (tenant.getViewingRequests() == null) {
                tenant.setViewingRequests(new HashSet<>()); // Ensure the set is initialized
            }
            tenant.getViewingRequests().add(this);
        }
    }

    public void setProperty(Property property) {
        this.property = property;
        if (property != null) {
            if (property.getViewingRequests() == null) {
                property.setViewingRequests(new HashSet<>()); // Ensure initialization
            }
            property.getViewingRequests().add(this);
        }
    }
}
