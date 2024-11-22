package gr.hua.dit.ds.housingsystem.entities.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private byte[] data; // Storing photo as binary data

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}

