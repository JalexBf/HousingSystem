package gr.hua.dit.ds.housingsystem.DTO;

public class ViewingRequestDTO {
    private Long id;
    private String status;
    private PropertyDTO property;
    private TenantDTO tenant;
    private AvailabilitySlotDTO availabilitySlot;
    private Long propertyId;
    private Long tenantId;
    private Long availabilitySlotId;

    public ViewingRequestDTO() {
    }

    public ViewingRequestDTO(Long id, String status, PropertyDTO property, TenantDTO tenant, AvailabilitySlotDTO availabilitySlot, Long propertyId, Long tenantId, Long availabilitySlotId) {
        this.id = id;
        this.status = status;
        this.property = property;
        this.tenant = tenant;
        this.availabilitySlot = availabilitySlot;
        this.propertyId = (propertyId != null) ? propertyId : (property != null ? property.getId() : null);
        this.tenantId = (tenantId != null) ? tenantId : (tenant != null ? tenant.getId() : null);
        this.availabilitySlotId = (availabilitySlotId != null) ? availabilitySlotId : (availabilitySlot != null ? availabilitySlot.getId() : null);
    }


    public ViewingRequestDTO(Long id, String status, PropertyDTO property, TenantDTO tenant, AvailabilitySlotDTO availabilitySlot) {
        this(id, status, property, tenant, availabilitySlot,
                (property != null) ? property.getId() : null,
                (tenant != null) ? tenant.getId() : null,
                (availabilitySlot != null) ? availabilitySlot.getId() : null);
    }

    public Long getId() { return id; }
    public String getStatus() { return status; }
    public PropertyDTO getProperty() { return property; }
    public TenantDTO getTenant() { return tenant; }
    public AvailabilitySlotDTO getAvailabilitySlot() { return availabilitySlot; }

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getAvailabilitySlotId() { return availabilitySlotId; }
    public void setAvailabilitySlotId(Long availabilitySlotId) { this.availabilitySlotId = availabilitySlotId; }
}
