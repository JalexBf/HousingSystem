package gr.hua.dit.ds.housingsystem.repositories;

import gr.hua.dit.ds.housingsystem.entities.enums.UserRole;
import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByAfm(String afm);

    List<AppUser> findByRole(UserRole role);

    List<AppUser> findByApproved(boolean approved);

}
