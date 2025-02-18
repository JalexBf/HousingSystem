package gr.hua.dit.ds.housingsystem.services;

public class ViewingRequestDTO {
    private Long id;
    private String status;
    private PropertyDTO property;
    private AvailabilitySlotDTO availabilitySlot;

    public ViewingRequestDTO(Long id, String status, PropertyDTO property, AvailabilitySlotDTO availabilitySlot) {
        this.id = id;
        this.status = status;
        this.property = property;
        this.availabilitySlot = availabilitySlot;
    }

    public Long getId() { return id; }
    public String getStatus() { return status; }
    public PropertyDTO getProperty() { return property; }
    public AvailabilitySlotDTO getAvailabilitySlot() { return availabilitySlot; }
}