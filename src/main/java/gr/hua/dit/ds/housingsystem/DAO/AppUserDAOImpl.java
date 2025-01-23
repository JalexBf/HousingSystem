package gr.hua.dit.ds.housingsystem.DAO;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AppUserDAOImpl implements AppUserDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<AppUser> getUsers() {
        Query query = entityManager.createQuery("from AppUser");
        return query.getResultList();
    }

    @Override
    public void saveUser(AppUser user) {
        AppUser currentUser = entityManager.merge(user);
        user.setId(currentUser.getId());
    }

    @Override
    public AppUser getUser(int id) {
        return entityManager.find(AppUser.class, id);
    }

    @Override
    public void deleteUser(int id) {
        Query query = entityManager.createQuery("delete from AppUser where id=:userId");
        query.setParameter("userId", id);
        query.executeUpdate();
    }

    @Override
    public AppUser findByUsername(String username) {
        Query query = entityManager.createQuery("from AppUser where username=:username");
        query.setParameter("username", username);
        List<AppUser> users = query.getResultList();
        return users.isEmpty() ? null : users.get(0);
    }
}