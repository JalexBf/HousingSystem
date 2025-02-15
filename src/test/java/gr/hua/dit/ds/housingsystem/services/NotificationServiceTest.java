package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.model.Notification;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.repositories.NotificationRepository;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private NotificationService notificationService;

    private AppUser user;
    private Notification notification;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new AppUser();
        user.setId(1L);
        user.setUsername("testUser");

        notification = new Notification(1L, "Test Message", LocalDateTime.now());
    }


    @Test
    void testCreateNotification() {

        when(appUserRepository.existsById(1L)).thenReturn(true);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        Notification createdNotification = notificationService.createNotification(1L, "Test Message");
        assertNotNull(createdNotification);
        assertEquals("Test Message", createdNotification.getMessage());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }


    @Test
    void testDeleteNotification_UserCanDeleteOwnNotification() {

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        notificationService.deleteNotification(1L, 1L);
        verify(notificationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotification_UserCannotDeleteOthersNotification() {

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                notificationService.deleteNotification(1L, 2L));
        assertEquals("You do not have permission to delete this notification", exception.getMessage());
    }

    @Test
    void testGetNotificationsByUserId() {

        when(notificationRepository.findByUserIdOrderByTimestampDesc(1L)).thenReturn(List.of(notification));
        var notifications = notificationService.getNotificationsByUserId(1L);
        assertEquals(1, notifications.size());
        assertEquals("Test Message", notifications.get(0).getMessage());
    }

    @Test
    void testGetUserIdByUsername() {

        when(appUserRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        Long userId = notificationService.getUserIdByUsername("testUser");
        assertEquals(1L, userId);
    }
}
