package gr.hua.dit.ds.housingsystem.entities.model;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

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
}

