package gr.hua.dit.ds.housingsystem.services;

public class RentalRequestDTO {
    private Long id;
    private String status;
    private PropertyDTO property;

    public RentalRequestDTO(Long id, String status, PropertyDTO property) {
        this.id = id;
        this.status = status;
        this.property = property;
    }

    public Long getId() { return id; }
    public String getStatus() { return status; }
    public PropertyDTO getProperty() { return property; }
}