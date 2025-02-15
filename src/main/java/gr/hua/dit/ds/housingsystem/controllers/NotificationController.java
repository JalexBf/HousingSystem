package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.DAO.NotificationRequest;
import gr.hua.dit.ds.housingsystem.entities.model.Notification;
import gr.hua.dit.ds.housingsystem.services.NotificationService;
import gr.hua.dit.ds.housingsystem.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }


    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody NotificationRequest request) {
        Long userId = getCurrentUserId(token);
        Notification createdNotification = notificationService.createNotification(userId, request.getMessage());
        return ResponseEntity.ok(createdNotification);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        notificationService.deleteNotification(id, userId);
        return ResponseEntity.noContent().build();
    }


    private Long getCurrentUserId(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        String jwt = token.substring(7); // Remove "Bearer " prefix
        try {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            return notificationService.getUserIdByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }
}
