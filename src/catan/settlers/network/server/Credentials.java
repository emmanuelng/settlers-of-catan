package catan.settlers.network.server;

import java.io.Serializable;

public class Credentials implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;

	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public boolean comparePassword(String other) {
		return password.equals(other);
	}
}
