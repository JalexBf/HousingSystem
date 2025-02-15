package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.model.Notification;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.repositories.NotificationRepository;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }


    public Notification createNotification(Long userId, String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        Notification notification = new Notification(userId, message, LocalDateTime.now());
        return notificationRepository.save(notification);
    }


    public void deleteNotification(Long id, Long userId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));

        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to delete this notification");
        }

        notificationRepository.deleteById(id);
    }


    public Long getUserIdByUsername(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return user.getId();
    }
}
