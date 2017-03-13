package catan.settlers.server.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Non-progress cards (ie: resources and commodities) will be stored in a
	 * simple integer array. Each value in the array corresponds to the amount
	 * of each type of card possessed by the player. The convention for card
	 * types is alphabetical, resource before commodities: [0] BRICK, [1] GRAIN,
	 * [2] LUMBER, [3] ORE, [4] WOOL, [5] CLOTH, [6] COIN, [7] PAPER
	 */
	public enum ResourceType {
		BRICK, GRAIN, LUMBER, ORE, WOOL, CLOTH, COIN, PAPER
	}

	private HashMap<ResourceType, Integer> resources;
	private Credentials credentials;

	public Player(Credentials credentials) {
		this.credentials = credentials;
		this.resources = new HashMap<>();
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void sendCommand(ServerToClientCommand cmd) {
		Session s = Server.instance.getPlayerManager().getSessionByPlayer(this);
		try {
			s.sendCommand(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getResourceAmount(ResourceType res) {
		return resources.get(res);
	}

	public void giveResource(ResourceType r, int amount) {
		int currentAmount = resources.get(r);
		resources.put(r, currentAmount + amount);
	}

	public String getUsername() {
		return credentials.getUsername();
	}

	public Session getSession() {
		return Server.getInstance().getPlayerManager().getSessionByPlayer(this);
	}
}