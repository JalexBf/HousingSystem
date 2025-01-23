package gr.hua.dit.ds.housingsystem.config;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.repositories.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminCreationConfig {

    @Bean
    public CommandLineRunner createAdminUser(AppUserRepository userRepository,
                                             BCryptPasswordEncoder encoder) {
        return args -> {
            // Check if admin user already exists
            String adminUsername = "admin";
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                AppUser adminUser = new AppUser();
                adminUser.setUsername(adminUsername);
                adminUser.setEmail("admin@gmail.com");
                adminUser.setPassword(encoder.encode("admin123"));
                adminUser.setFirstName("System");
                adminUser.setLastName("Administrator");
                adminUser.setPhone("0000000000");
                adminUser.setAfm("0000000000");

                adminUser.setIdFrontPath("");
                adminUser.setIdBackPath("");
                adminUser.setRole(UserRole.ADMIN);
                adminUser.setApproved(true);
                userRepository.save(adminUser);

                System.out.println("Created default admin user: " + adminUsername);
            }
        };
    }
}
