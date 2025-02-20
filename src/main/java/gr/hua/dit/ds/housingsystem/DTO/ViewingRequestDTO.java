package gr.hua.dit.ds.housingsystem.DTO;


public class ViewingRequestDTO {
    private Long id;
    private String status;
    private PropertyDTO property;
    private AvailabilitySlotDTO availabilitySlot;
    private TenantDTO tenant;

    public ViewingRequestDTO(Long id, String status, PropertyDTO property, TenantDTO tenant, AvailabilitySlotDTO availabilitySlot) {
        this.id = id;
        this.status = status;
        this.property = property;
        this.tenant = tenant;
        this.availabilitySlot = availabilitySlot;
    }

    public Long getId() { return id; }
    public String getStatus() { return status; }
    public PropertyDTO getProperty() { return property; }
    public AvailabilitySlotDTO getAvailabilitySlot() { return availabilitySlot; }
    public TenantDTO getTenant() { return tenant; }
}

