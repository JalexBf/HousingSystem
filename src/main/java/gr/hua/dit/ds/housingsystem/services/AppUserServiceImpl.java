package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AppUserServiceImpl{

//    private AppUserRepository appUserRepository;
//
//    private BCryptPasswordEncoder passwordEncoder;
//
//    public AppUserServiceImpl(AppUserRepository appUserRepository, BCryptPasswordEncoder passwordEncoder) {
//        this.appUserRepository = appUserRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Transactional
//    public Integer saveUser(AppUser appUser) {
//        String passwd= appUser.getPassword();
//        String encodedPassword = passwordEncoder.encode(passwd);
//        appUser.setPassword(encodedPassword);
//        appUser = appUserRepository.save(appUser);
//        return appUser.getId();
//    }
//
//    @Transactional
//    public Integer updateUser(AppUser appUser) {
//        appUser = appUserRepository.save(appUser);
//        return appUser.getId();
//    }
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<AppUser> opt = appUserRepository.findByUsername(username);
//
//        if(opt.isEmpty())
//            throw new UsernameNotFoundException("User with email: " +username +" not found !");
//        else {
//            AppUser appUser = opt.get();
//            return new org.springframework.security.core.userdetails.User(
//                    appUser.getEmail(),
//                    appUser.getPassword(),
//                    appUser.getRole()
//            );
//        }
//    }
//
//    public void deleteAppUser(Long propertyId) {
//        AppUser appUser = appUserRepository.findById(propertyId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found."));
//        appUserRepository.delete(appUser);
//    }
//
//    @Transactional
//    public Object getUsers() {
//        return appUserRepository.findAll();
//    }
//
//    public Object getUser(Long userId) {
//        return appUserRepository.findById(userId).get();
//    }

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
