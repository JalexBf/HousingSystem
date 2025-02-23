package gr.hua.dit.ds.housingsystem.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.enums.PropertyFeatures;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyCategory category;

    @Column(nullable = false, length = 100)
    @Size(min = 3, max = 100, message = "Area must be between 3 and 100 characters.")
    private String area;

    @Column(nullable = false, length = 255)
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters.")
    private String address;

    @Column(nullable = false, unique = true, length = 10)
    @Pattern(regexp = "^[0-9]{10}$", message = "ATAK must be exactly 10 digits.")
    private String atak;

    @Column(nullable = false)
    @Positive(message = "Price must be a positive number.")
    private Double price;

    @Column(nullable = false)
    @Positive(message = "Square meters must be a positive number.")
    private Integer squareMeters;

    @Column(nullable = false)
    @Min(value = 0, message = "Floor cannot be negative.")
    @Max(value = 10, message = "Floor cannot exceed 10.")
    private Integer floor;

    @Column(nullable = false)
    @Min(value = 1, message = "Number of rooms must be at least 1.")
    @Max(value = 10, message = "Number of rooms cannot exceed 10.")
    private Integer numberOfRooms;

    @Column(nullable = false)
    @Min(value = 1, message = "Number of bathrooms must be at least 1.")
    @Max(value = 10, message = "Number of bathrooms cannot exceed 10.")
    private Integer numberOfBathrooms;

    @Column(nullable = false)
    @Min(value = 1900, message = "Renovation year must be later than 1900.")
    @Max(value = 2100, message = "Renovation year must be earlier than 2100.")
    private Integer renovationYear;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_amenities", joinColumns = @JoinColumn(name = "property_id"))
    @Enumerated(EnumType.STRING)
    private Set<PropertyFeatures> amenities;

    @JsonIgnore
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Photo> photos = new HashSet<>();

    //@JsonBackReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private AppUser owner;

    @JsonIgnore
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.LAZY)
    private Set<ViewingRequest> viewingRequests;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AvailabilitySlot> availabilitySlots = new HashSet<>();

    @Column(nullable = false)
    private boolean approved = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;
        Property property = (Property) o;
        return id != null && id.equals(property.id);
    }

    @Override
    public int hashCode() {
        // Use only id for hashCode
        return getClass().hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PropertyCategory getCategory() {
        return category;
    }

    public void setCategory(PropertyCategory category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }
}
