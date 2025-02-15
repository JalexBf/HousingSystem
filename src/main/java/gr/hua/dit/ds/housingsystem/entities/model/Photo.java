package gr.hua.dit.ds.housingsystem.entities.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Path to the property photo file
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}

