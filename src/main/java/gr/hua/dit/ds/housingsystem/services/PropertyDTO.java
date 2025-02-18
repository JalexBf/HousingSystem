package gr.hua.dit.ds.housingsystem.services;

import java.util.List;

public class PropertyDTO {
    private Long id;
    private String category;
    private String area;
    private String address;
    private Double price;
    private Integer squareMeters;
    private Integer floor;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
    private Integer renovationYear;
    private String atak;
    private List<String> amenities;
    private List<String> photos;

    private List<AvailabilitySlotDTO> availabilitySlots;

    public PropertyDTO(Long id, String category, String area, String address, Double price,
                       Integer squareMeters, Integer floor, Integer numberOfRooms, Integer numberOfBathrooms,
                       Integer renovationYear, String atak, List<String> amenities, List<String> photos, List<AvailabilitySlotDTO> availabilitySlots) {
        this.id = id;
        this.category = category;
        this.area = area;
        this.address = address;
        this.price = price;
        this.squareMeters = squareMeters;
        this.floor = floor;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.renovationYear = renovationYear;
        this.atak = atak;
        this.amenities = amenities;
        this.photos = photos;
        this.availabilitySlots = availabilitySlots;
    }

    public PropertyDTO() {
    }

    public PropertyDTO(Long id, String address, Double price) {
        this.id = id;
        this.address = address;
        this.price = price;
    }

    // Getters
    public Long getId() { return id; }
    public String getCategory() { return category; }
    public String getArea() { return area; }
    public String getAddress() { return address; }
    public Double getPrice() { return price; }
    public Integer getSquareMeters() { return squareMeters; }
    public Integer getFloor() { return floor; }
    public Integer getNumberOfRooms() { return numberOfRooms; }
    public Integer getNumberOfBathrooms() { return numberOfBathrooms; }
    public Integer getRenovationYear() { return renovationYear; }
    public String getAtak() { return atak; }
    public List<String> getAmenities() { return amenities; }
    public List<String> getPhotos() { return photos; }

    public List<AvailabilitySlotDTO> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSquareMeters(Integer squareMeters) {
        this.squareMeters = squareMeters;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setNumberOfBathrooms(Integer numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public void setRenovationYear(Integer renovationYear) {
        this.renovationYear = renovationYear;
    }

    public void setAtak(String atak) {
        this.atak = atak;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public void setAvailabilitySlots(List<AvailabilitySlotDTO> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }
}
