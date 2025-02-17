package gr.hua.dit.ds.housingsystem.entities.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
//    @JsonIgnore
    @JsonBackReference("property-viewing")
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
//    @JsonIgnore
    @JsonBackReference("user-viewing")
    @JoinColumn(name = "tenant_id", nullable = false)
    private AppUser tenant;

//    private LocalDateTime proposedTime;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
    @JoinColumn(name = "availability_slot_id", nullable = false)
    @JsonBackReference("availability-viewing")
    private AvailabilitySlot availabilitySlot;


    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING; // Status: PENDING, APPROVED, REJECTED, CANCELED

    public void setProperty(Property property) {
        this.property = property;
        if (property != null && !property.getViewingRequests().contains(this)) {
            property.getViewingRequests().add(this);
        }
    }
}
