package gr.hua.dit.ds.housingsystem.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    private String firstName;

    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    private String lastName;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits.")
    private String phone;

    @Column(nullable = false, unique = true)
    @Email(message = "Email must be in a valid format.")
    private String email;

    @Column(nullable = false, unique = true, length = 10)
    @Pattern(regexp = "^[0-9]{10}$", message = "AFM must be exactly 10 digits.")
    private String afm;

    @Column(nullable = true) // Path to the front of the ID card
    private String idFrontPath;

    @Column(nullable = true) // Path to the back of the ID card
    private String idBackPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private boolean approved = false;

    //@JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "tenant", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<RentalRequest> rentalRequests;

    //@JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "tenant", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<ViewingRequest> viewingRequests;

    public AppUser() {
    }

    public AppUser(String username, String password, String firstName, String lastName, String phone, String email, String afm, byte[] idProof, UserRole role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.afm = afm;
        this.idFrontPath = idFrontPath;
        this.idBackPath = idBackPath;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getIdFrontPath() {
        return idFrontPath;
    }

    public void setIdFrontPath(String idFrontPath) {
        this.idFrontPath = idFrontPath;
    }

    public String getIdBackPath() {
        return idBackPath;
    }

    public void setIdBackPath(String idBackPath) {
        this.idBackPath = idBackPath;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

}



