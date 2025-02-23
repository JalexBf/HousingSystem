package gr.hua.dit.ds.housingsystem.DTO;


public class ViewingRequestDTO {
    private Long propertyId;
    private Long tenantId;
    private Long availabilitySlotId;
    private String status;

    public ViewingRequestDTO() {}

    public ViewingRequestDTO(Long propertyId, Long tenantId, Long availabilitySlotId, String status) {
        this.propertyId = propertyId;
        this.tenantId = tenantId;
        this.availabilitySlotId = availabilitySlotId;
        this.status = status;
    }

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getAvailabilitySlotId() { return availabilitySlotId; }
    public void setAvailabilitySlotId(Long availabilitySlotId) { this.availabilitySlotId = availabilitySlotId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}