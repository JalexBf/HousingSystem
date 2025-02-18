package gr.hua.dit.ds.housingsystem.entities.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.DayOfWeek;
import java.util.Objects;


@Entity
@Data
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading
    @JsonBackReference("property-availability")
    @JoinColumn(name = "property_id", nullable = false)
    @JsonIgnore
    private Property property;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private Integer startHour;

    @Column(nullable = false)
    private Integer endHour;

    public void validate() {
        if (startHour < 6 || startHour >= 22 || endHour <= startHour || endHour > 22) {
            throw new IllegalArgumentException("Invalid time slot. Hours must be between 06:00 and 22:00.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilitySlot that = (AvailabilitySlot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
