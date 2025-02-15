package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.DAO.NotificationRequest;
import gr.hua.dit.ds.housingsystem.entities.model.Notification;
import gr.hua.dit.ds.housingsystem.services.NotificationService;
import gr.hua.dit.ds.housingsystem.config.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private NotificationController notificationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }


    @Test
    void testCreateNotification() throws Exception {
        NotificationRequest request = new NotificationRequest();
        request.setMessage("Test Message");

        Notification notification = new Notification(1L, "Test Message", LocalDateTime.now());

        when(notificationService.createNotification(anyLong(), anyString())).thenReturn(notification);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notifications")
                        .header("Authorization", "Bearer valid_token")
                        .contentType("application/json")
                        .content("{\"message\": \"Test Message\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test Message"));
    }


    @Test
    void testDeleteNotification() throws Exception {
        // Mocking the behavior of JwtUtils to return a user ID based on the token
        when(jwtUtils.getUserNameFromJwtToken("valid_token")).thenReturn("testUser");
        when(notificationService.getUserIdByUsername("testUser")).thenReturn(1L);

        doNothing().when(notificationService).deleteNotification(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notifications/1")
                        .header("Authorization", "Bearer valid_token"))
                .andExpect(status().isNoContent());

        verify(notificationService, times(1)).deleteNotification(1L, 1L);
    }


    @Test
    void testGetAllNotifications() throws Exception {
        // Mocking the behavior of JwtUtils to return a user ID based on the token
        when(jwtUtils.getUserNameFromJwtToken("valid_token")).thenReturn("testUser");
        when(notificationService.getUserIdByUsername("testUser")).thenReturn(1L);

        Notification notification = new Notification(1L, "Test Message", LocalDateTime.now());
        when(notificationService.getNotificationsByUserId(1L)).thenReturn(List.of(notification));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notifications")
                        .header("Authorization", "Bearer valid_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Test Message"));
    }

}
