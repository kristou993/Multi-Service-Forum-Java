package dto;

import tn.esprit.wediscus.entity.User;

public class UserResetDto {

	private String username;
	private String newpassword;
	private String verification;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNewpassword() {
		return newpassword;
	}
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	public String getVerification() {
		return verification;
	}
	public void setVerification(String verification) {
		this.verification = verification;
	}
	public User toUser(){
		User ret = new User();
		ret.setUsername(username);
		ret.setPassword(newpassword);
		return ret;
	}
	
	
}
