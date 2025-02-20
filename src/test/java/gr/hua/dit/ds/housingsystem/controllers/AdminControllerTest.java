package gr.hua.dit.ds.housingsystem.controllers;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class AdminControllerTest {

    private MockMvc mockMvc;
    private AdminService adminService;
    private AdminController adminController;

    @BeforeEach
    public void setup() {
        adminService = Mockito.mock(AdminService.class); // Manually mock service
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testApproveProperty() throws Exception {
        doNothing().when(adminService).approveProperty(1L);

        mockMvc.perform(put("/admin/approveProperty/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Property approved successfully."));

        verify(adminService, times(1)).approveProperty(1L);
    }


    @Test
    public void testRejectProperty() throws Exception {
        doNothing().when(adminService).rejectProperty(1L);

        mockMvc.perform(delete("/admin/rejectProperty/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Property rejected and removed successfully."));

        verify(adminService, times(1)).rejectProperty(1L);
    }


    @Test
    public void testGetUnapprovedProperties() throws Exception {
        Property property1 = new Property();
        Property property2 = new Property();
        when(adminService.getUnapprovedProperties()).thenReturn(Arrays.asList(property1, property2));

        mockMvc.perform(get("/admin/unapprovedProperties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(adminService, times(1)).getUnapprovedProperties();
    }


    @Test
    public void testApproveUser() throws Exception {
        doNothing().when(adminService).approveUser(1L);

        mockMvc.perform(put("/admin/approveUser/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User approved successfully."));

        verify(adminService, times(1)).approveUser(1L);
    }


    @Test
    public void testRejectUser() throws Exception {
        doNothing().when(adminService).rejectUser(1L);

        mockMvc.perform(put("/admin/rejectUser/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User rejected successfully."));

        verify(adminService, times(1)).rejectUser(1L);
    }


    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(adminService).deleteUser(1L);

        mockMvc.perform(delete("/admin/deleteUser/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));

        verify(adminService, times(1)).deleteUser(1L);
    }


    @Test
    public void testGetUnapprovedUsers() throws Exception {
        AppUser user1 = new AppUser();
        AppUser user2 = new AppUser();
        when(adminService.getUnapprovedUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/admin/unapprovedUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(adminService, times(1)).getUnapprovedUsers();
    }
}
