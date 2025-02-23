package gr.hua.dit.ds.housingsystem.DTO;


public class TenantDTO {
    private Long id;
    private String username;

    public TenantDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
}

