package catan.settlers.server.model;

import java.io.IOException;
import java.io.Serializable;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.Session;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private Session session;
	
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
	
	public void setCurrentSession(Session session) {
		this.session = session;
	}
	
	public boolean isConnected() {
		return session == null;
	}
	
	public void sendCommand(ServerToClientCommand cmd) {
		if (isConnected()) {
			try {
				session.sendCommand(cmd);
			} catch (IOException e) {
				// Ignore: failed to send command
				e.printStackTrace();
			}
		}
	}
}
