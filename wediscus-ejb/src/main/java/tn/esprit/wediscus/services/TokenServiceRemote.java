package tn.esprit.wediscus.services;

import javax.ejb.Local;

import tn.esprit.wediscus.entity.Token;
import tn.esprit.wediscus.entity.User;

@Local
public interface TokenServiceRemote {
	public void setToken(String tokenValue, User user);
	public User getUser(String tokenValue);
	public Token find(String tokenValue);
	public Token find(User u);
	public void save(Token token);
	public void disableToken(User u) throws Exception;
}
