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
	
	
	/*
	 *  Non-progress cards (ie: resources and commodities) will be stored in a simple
	 *  integer array. Each value in the array corresponds to the amount of each type
	 *  of card possessed by the player. The convention for card types is alphabetical,
	 *  resource before commodities:
	 *  [0] BRICK, [1] GRAIN, [2] LUMBER, [3] ORE, [4] WOOL, 
	 *  [5] CLOTH, [6] COIN, [7] PAPER
	 */
	private int[] myResources;
	
	public Player(String username, String password) {
		this.username = username;
		this.password = password;
		this.session = null;
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
		return session != null;
	}
	
	public void sendCommand(ServerToClientCommand cmd) {
		if (isConnected()) {
			try {
				session.sendCommand(cmd);
			} catch (IOException e) {
				// failed to send command
				e.printStackTrace();
			}
		}
	}
	
	public void giveResource(int resource, int amount) {
		myResources[resource] += amount;
	}
	
	public void takeResource(int resource, int amount) {
		if (myResources[resource] >= amount) {
			myResources[resource] -= amount;
		}
	}
}
