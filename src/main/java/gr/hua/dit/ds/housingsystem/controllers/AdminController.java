package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.services.AdminService;
import gr.hua.dit.ds.housingsystem.services.PropertyDTO;
import gr.hua.dit.ds.housingsystem.services.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final PropertyService propertyService;

    public AdminController(AdminService adminService, PropertyService propertyService) {
        this.adminService = adminService;
        this.propertyService = propertyService;
    }

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);


    @PutMapping("/approveProperty/{propertyId}")
    public ResponseEntity<String> approveProperty(@PathVariable Long propertyId) {
        adminService.approveProperty(propertyId);
        return ResponseEntity.ok("Property approved successfully.");
    }


    @DeleteMapping("/rejectProperty/{propertyId}")
    public ResponseEntity<String> rejectProperty(@PathVariable Long propertyId) {
        adminService.rejectProperty(propertyId);
        return ResponseEntity.ok("Property rejected and removed successfully.");
    }


    @GetMapping("/unapprovedProperties")
    public ResponseEntity<List<Property>> getUnapprovedProperties() {
        List<Property> properties = adminService.getUnapprovedProperties();
        return ResponseEntity.ok(properties);
    }


    @PutMapping("/approveUser/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable Long userId) {
        adminService.approveUser(userId);
        return ResponseEntity.ok("User approved successfully.");
    }


    @PutMapping("/rejectUser/{userId}")
    public ResponseEntity<String> rejectUser(@PathVariable Long userId) {
        adminService.rejectUser(userId);
        return ResponseEntity.ok("User rejected successfully.");
    }


    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully.");
    }


    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId, @RequestBody AppUser updatedUser) {
        adminService.updateUser(userId, updatedUser);
        return ResponseEntity.ok("User updated successfully.");
    }


    @GetMapping("/unapprovedUsers")
    public ResponseEntity<List<AppUser>> getUnapprovedUsers() {
        List<AppUser> users = adminService.getUnapprovedUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/approvedUsers")
    public ResponseEntity<List<AppUser>> getApprovedUsers() {
        List<AppUser> users = adminService.getApprovedUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/checkUsername")
    public ResponseEntity<Boolean> isUsernameUnique(
            @RequestParam String username,
            @RequestParam(required=false) Long excludeId
    ) {
        boolean isUnique = adminService.isUsernameUnique(username, excludeId);
        return ResponseEntity.ok(isUnique);
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<Boolean> isEmailUnique(
            @RequestParam String email,
            @RequestParam(required = false) Long excludeId) {

        boolean isUnique = adminService.isEmailUnique(email, excludeId);
        return ResponseEntity.ok(isUnique);
    }


    @GetMapping("/userDetailsPage/{userId}")
    public String userDetailsPage(@PathVariable Long userId, Model model) {
        AppUser user = adminService.getUserById(userId);
        model.addAttribute("user", user);
        return "userDetails";
    }

    /*
    @GetMapping("/propertyDetails/{id}")
    public ResponseEntity<PropertyDTO> getPropertyDetails(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);

        property.getPhotos().size();
        property.getAmenities().size();
        property.getViewingRequests().size();
        property.getAvailabilitySlots().size();

        return ResponseEntity.ok(new PropertyDTO(property));
    }"*/




}
