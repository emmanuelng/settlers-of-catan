package catan.settlers.server.model;

import java.io.Serializable;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	
	public Player(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean comparePassword(String proposedPassword) {
		return password.equals(proposedPassword);
	}
}
