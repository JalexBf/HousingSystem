package gr.hua.dit.ds.housingsystem.dao;

import gr.hua.dit.ds.housingsystem.entities.model.AppUser;
import java.util.List;

public interface AppUserDAO {
    public List<AppUser> getUsers();
    public void saveUser(AppUser user);
    public AppUser getUser(int id);
    public void deleteUser(int id);
    public AppUser findByUsername(String username);
}