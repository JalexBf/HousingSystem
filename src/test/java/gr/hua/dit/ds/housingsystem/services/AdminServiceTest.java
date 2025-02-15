package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AdminServiceTest {

    private AdminService adminService;
    private AppUserRepository appUserRepository;
    private PropertyRepository propertyRepository;

    @BeforeEach
    public void setUp() {
        appUserRepository = mock(AppUserRepository.class);
        propertyRepository = mock(PropertyRepository.class);
        adminService = new AdminService(appUserRepository, propertyRepository);
    }

    @Test
    public void testApproveProperty() {
        Property property = new Property();
        property.setId(1L);
        property.setApproved(false);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        adminService.approveProperty(1L);

        assertTrue(property.isApproved());
        verify(propertyRepository, times(1)).save(property);
    }

    @Test
    public void testRejectProperty() {
        Property property = new Property();
        property.setId(1L);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        adminService.rejectProperty(1L);

        verify(propertyRepository, times(1)).delete(property);
    }

    @Test
    public void testGetUnapprovedProperties() {
        Property property1 = new Property();
        Property property2 = new Property();

        when(propertyRepository.findByApproved(false)).thenReturn(Arrays.asList(property1, property2));

        List<Property> unapprovedProperties = adminService.getUnapprovedProperties();

        assertEquals(2, unapprovedProperties.size());
        verify(propertyRepository, times(1)).findByApproved(false);
    }

    @Test
    public void testApproveUser() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setApproved(false);

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));

        adminService.approveUser(1L);

        assertTrue(user.isApproved());
        verify(appUserRepository, times(1)).save(user);
    }

    @Test
    public void testRejectUser() {
        AppUser user = new AppUser();
        user.setId(1L);

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));

        adminService.rejectUser(1L);

        assertFalse(user.isApproved());
        verify(appUserRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        AppUser user = new AppUser();
        user.setId(1L);

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));

        adminService.deleteUser(1L);

        verify(appUserRepository, times(1)).delete(user);
    }

    @Test
    public void testGetUnapprovedUsers() {
        AppUser user1 = new AppUser();
        AppUser user2 = new AppUser();

        when(appUserRepository.findByApproved(false)).thenReturn(Arrays.asList(user1, user2));

        List<AppUser> unapprovedUsers = adminService.getUnapprovedUsers();

        assertEquals(2, unapprovedUsers.size());
        verify(appUserRepository, times(1)).findByApproved(false);
    }
}
