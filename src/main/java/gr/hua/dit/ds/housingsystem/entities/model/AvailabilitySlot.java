package gr.hua.dit.ds.housingsystem.entities.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.DayOfWeek;


@Entity
@Data
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
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
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
    }
}
