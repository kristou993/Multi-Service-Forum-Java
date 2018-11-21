package tn.esprit.wediscus.services;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.NotAuthorizedException;

import tn.esprit.wediscus.entity.Token;
import tn.esprit.wediscus.entity.User;

@Stateless
public class TokenService implements TokenServiceRemote{
	
	@PersistenceContext(name="wediscus-ejb") 
	EntityManager em;
	
	@EJB
	UserServiceLocal userService;
	
	
	@Override
	public void setToken(String tokenValue, User user) {
		// TODO Auto-generated method stub
		Token token = new Token();
		token.setValue(tokenValue);
		
		Token queryToken = this.find(user);
		if(queryToken == null){
			System.out.println("User in setToken : "+user);
			token.setUser(user);
			Date expires = new Date();
	        expires.setTime(expires.getTime() + 1000 * 60 * 60 * 24);
			token.setExpiration(expires);
			this.save(token);
		}else{
			Date expires = new Date();
	        expires.setTime(expires.getTime() + 1000 * 60 * 60 * 24);
	        queryToken.setExpiration(expires);
	        queryToken.setValue(tokenValue);
	        em.merge(queryToken);
		}
		
	}

	@Override
	public User getUser(String tokenValue) throws NotAuthorizedException{
		System.out.println("Token value : "+tokenValue);
		Token token = this.find(tokenValue);
		if (token == null) {
            throw new NotAuthorizedException("Token unknown");
        }
        if (token.getExpiration().before(new Date())) {
            throw new NotAuthorizedException("Token expires");
        }
        Date expires = new Date();
        expires.setTime(expires.getTime() + 1000 * 60 * 60 * 24);
        token.setExpiration(expires);
        this.save(token);
		return token.getUser();
	}

	@Override
	public Token find(String tokenValue) {
		Query query = em.createQuery("SELECT t from Token t where t.value = :value");
		query.setParameter("value", tokenValue);
		return (Token) query.getSingleResult();
	}

	@Override
	public void save(Token token) {
		em.persist(token);	
	}

	@Override
	public Token find(User u) {
		Query query = em.createQuery("SELECT t from Token t where t.user = :id");
		query.setParameter("id", u);
		Token retToken = null;
		try{
			retToken = (Token) query.getSingleResult();
		}catch(Exception e){
			return null;
		}
		return retToken;
	}

	@Override
	public void disableToken(User u) throws Exception {
		Token token = this.find(u);
		if(token != null){
			Date expires = new Date();
			token.setExpiration(expires);
			em.merge(token);
			return;
		}
		throw new Exception("Token not found!");
			
	}

}
