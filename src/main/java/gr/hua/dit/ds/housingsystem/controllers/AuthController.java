package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.config.JwtUtils;
import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.payload.request.LoginRequest;
import gr.hua.dit.ds.housingsystem.payload.request.SignupRequest;
import gr.hua.dit.ds.housingsystem.payload.response.JwtResponse;
import gr.hua.dit.ds.housingsystem.payload.response.MessageResponse;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AppUserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Set authentication context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Extract user details and roles
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // Return JWT token and user info
            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid username or password!"));
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Check if username already exists
        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Error: Username is already taken!",
                            "payload", signUpRequest // Include the JSON payload
                    ));
        }

        // Check if email already exists
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Error: Email is already in use!",
                            "payload", signUpRequest // Include the JSON payload
                    ));
        }

        // Check if AFM already exists
        if (userRepository.findByAfm(signUpRequest.getAfm()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Error: AFM is already in use!",
                            "payload", signUpRequest // Include the JSON payload
                    ));
        }

        // Create new AppUser entity
        AppUser user = new AppUser();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword())); // Encode password
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setPhone(signUpRequest.getPhone());
        user.setAfm(signUpRequest.getAfm());

        try {
            // Save ID front and back images from base64
            String idFrontPath = saveBase64Image(signUpRequest.getIdFrontPath(), "idFront_" + signUpRequest.getUsername());
            String idBackPath = saveBase64Image(signUpRequest.getIdBackPath(), "idBack_" + signUpRequest.getUsername());

            user.setIdFrontPath(idFrontPath); // Set file path for ID front
            user.setIdBackPath(idBackPath);  // Set file path for ID back
        } catch (IOException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Error: Failed to process ID card images.",
                            "payload", signUpRequest // Include the JSON payload
                    ));
        }

        // Set approved = false, admin needs to approve new users
        user.setApproved(false);

        // Assign role based on selectedRole
        switch (signUpRequest.getSelectedRole().toLowerCase()) {
            case "owner":
                user.setRole(UserRole.OWNER);
                break;
            case "tenant":
                user.setRole(UserRole.TENANT);
                break;
            default:
                return ResponseEntity
                        .badRequest()
                        .body(Map.of(
                                "message", "Error: Invalid role specified!",
                                "payload", signUpRequest // Include the JSON payload
                        ));
        }

        // Save the new user in the database
        userRepository.save(user);

        // Return success response with the JSON payload
        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully!",
                "payload", signUpRequest // Include the JSON payload
        ));
    }


     //Helper method to save a base64-encoded image to a file.

    private String saveBase64Image(String base64Image, String fileName) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("Image data is missing.");
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        String uploadDir = "uploads/";
        Path filePath = Paths.get(uploadDir + fileName + ".png");

        // Ensure the directory exists
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, decodedBytes); // Save the file

        return filePath.toString(); // Return the file path
    }



}