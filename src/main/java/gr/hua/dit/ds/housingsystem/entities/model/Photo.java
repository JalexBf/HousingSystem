package gr.hua.dit.ds.housingsystem.entities.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;
        Photo photo = (Photo) o;
        return id != null && id.equals(photo.id);
    }

    @Override
    public int hashCode() {
        // Use only id for hashCode
        return getClass().hashCode();
    }
}