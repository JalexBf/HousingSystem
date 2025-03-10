package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AppUserRepository userRepository;

    @Transactional
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with ID: " + id));

        return UserDetailsImpl.build(user);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }


    public void saveUser(AppUser user) {
        userRepository.save(user);
    }
}