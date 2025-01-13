package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AppUserServiceImpl{


    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> findUserById(Long id) {
        return appUserRepository.findById(id);
    }

    public Optional<AppUser> findUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public Optional<AppUser> findUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public Optional<AppUser> findUserByAfm(String afm) {
        return appUserRepository.findByAfm(afm);
    }

    public AppUser saveUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }

    public List<AppUser> findUsersByRole(UserRole role) {
        return appUserRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .toList();
    }
}
