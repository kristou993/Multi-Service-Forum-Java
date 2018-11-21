package tn.esprit.wediscus.services;

import java.util.Base64;
import java.util.Collection;
import java.util.StringTokenizer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.SecurityContext;

import tn.esprit.wediscus.entity.User;
import tn.esprit.wediscus.util.MD5;

@Stateless
public class UserService implements UserServiceRemote, UserServiceLocal{

	
	@PersistenceContext(name="wediscus-ejb") 
	EntityManager em;
	@Override
	public boolean addUser(User u) {
		if(findAll().contains(u)){
			return false;
		}
		
		u.setPassword(MD5.crypt(u.getPassword()));
		em.persist(u);
		return true;
	}

	@Override
	public User findUser(int id) {
		User userFromDB = em.find(User.class, id);
		return userFromDB;
	}
	
	@Override
	public User findUser(User user) {
		Query query = em.createQuery("SELECT u FROM User u WHERE u.username = :username OR u.phoneNumber = :phone");
		query.setParameter("username", user.getUsername());
		query.setParameter("phone", user.getPhoneNumber());
		User userFromDB = (User) query.getSingleResult();
		return userFromDB;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<User> findAll() {
		Query query = em.createQuery("SELECT u FROM User u");
		Collection<User> listFromDB = (Collection<User>) query.getResultList();
		return listFromDB == null ? null : listFromDB;
	}

	@Override
	public boolean updateUser(User u) {
		User userToUpdate = findUser(u);
		if(userToUpdate.equals(null))
			return false;
		userToUpdate.setPassword(u.getPassword());
		Query query = em.createQuery("UPDATE User u SET u.password = :p WHERE u.id = :id");
		query.setParameter("p", MD5.crypt(userToUpdate.getPassword()));
		query.setParameter("id", userToUpdate.getId());
		if(query.executeUpdate() > 0)
			return true;
		return false;
	}

	@Override
	public void deleteUser(User u) {
		em.remove(u);
		em.flush();
	}

	@Override
	public boolean login(User u) throws Exception{
		System.out.println("Login from service : "+u);
		Query query = em.createQuery("SELECT u FROM User u WHERE u.username = :username "
				+ "AND u.password = :password");
		query.setParameter("username", u.getUsername());
		query.setParameter("password", MD5.crypt(u.getPassword()));
		int resultCount = query.getResultList().size();
		System.out.println("Found "+resultCount+" Result(s) ");
		if(resultCount != 1){
			return false;
		}
		return true;
		
	}

	
	@Override
	public User getUserFromToken(String tokenValue) throws Exception {
		
		byte[] decodedBytes  = Base64.getDecoder().decode(tokenValue.getBytes("UTF-8"));
		String decodedString = new String(decodedBytes, "UTF-8");
		System.out.println("Our decoded token is : "+decodedString);
		StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
		
		String username 	= tokenizer.nextToken();
		User tmp = new User();
		tmp.setUsername(username);
		
		User loggedUser = this.findUser(tmp);
        if (loggedUser == null) {
            throw new Exception("Token unknown");
        }
        /*
         * if (token.getExpires().before(new Date())) {
            	throw new NotAuthorizedException("Token expires");
        	}
        
        	Date expires = new Date();
        */
        return loggedUser;
    }

	@Override
	public User getConnectedUser(SecurityContext securityContext) {
		
		String username = securityContext.getUserPrincipal().getName();
		User tmpUser = new User();
        tmpUser.setUsername(username);
        User loggedUser = this.findUser(tmpUser);
        return loggedUser;
	}

	@Override
	public boolean checkUniqueValue(String value, String input) {
		// TODO Auto-generated method stub
		Query query = em.createQuery("SELECT u FROM User u WHERE u."+value+" = :in ");
		query.setParameter("in", input);
		if(query.getResultList().size() == 0)
			return true;
		return false;
	}

	
}
