package gr.hua.dit.ds.housingsystem.DTO;

import java.util.Set;

public class AppUserDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Set<RentalRequestDTO> rentalRequests;
    private Set<ViewingRequestDTO> viewingRequests;

    public AppUserDTO() {
    }

    public AppUserDTO(Long id, String username, String firstName, String lastName, String email, String phone,
                      Set<RentalRequestDTO> rentalRequests, Set<ViewingRequestDTO> viewingRequests) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.rentalRequests = rentalRequests;
        this.viewingRequests = viewingRequests;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Set<RentalRequestDTO> getRentalRequests() { return rentalRequests; }
    public Set<ViewingRequestDTO> getViewingRequests() { return viewingRequests; }


    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRentalRequests(Set<RentalRequestDTO> rentalRequests) {
        this.rentalRequests = rentalRequests;
    }

    public void setViewingRequests(Set<ViewingRequestDTO> viewingRequests) {
        this.viewingRequests = viewingRequests;
    }
}