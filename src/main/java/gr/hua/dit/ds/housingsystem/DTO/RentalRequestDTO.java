package gr.hua.dit.ds.housingsystem.DTO;

public class RentalRequestDTO {
    private Long id;
    private String status;
    private PropertyDTO property;
    private TenantDTO tenant;

    public RentalRequestDTO(Long id, String status, PropertyDTO property, TenantDTO tenant) {
        this.id = id;
        this.status = status;
        this.property = property;
        this.tenant = tenant;
    }

    public Long getId() { return id; }
    public String getStatus() { return status; }
    public PropertyDTO getProperty() { return property; }
    public TenantDTO getTenant() { return tenant; }
}

