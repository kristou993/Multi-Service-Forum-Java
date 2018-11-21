package tn.esprit.wediscus.services;

import java.util.Collection;

import javax.ejb.Local;
import javax.ws.rs.core.SecurityContext;

import tn.esprit.wediscus.entity.User;

@Local
public interface UserServiceLocal {
	public boolean addUser(User u);
	public User findUser(int id);
	public User findUser(User user);
	public Collection<User> findAll();
	public boolean updateUser(User u);
	public void deleteUser(User u);
	public boolean login(User u) throws Exception;
	public User getUserFromToken(String tokenValue) throws Exception;
	public User getConnectedUser(SecurityContext securityContext);
}
