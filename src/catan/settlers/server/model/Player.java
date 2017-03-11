package catan.settlers.server.model;

import java.io.IOException;
import java.io.Serializable;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.map.Hexagon.TerrainType;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;

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

	private int[] resources;

	public Player(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public int getResourceAmount(ResourceType res) {
		return resources[res.ordinal()];
	}
	
	public void sendCommand(ServerToClientCommand cmd) {
		Session s = Server.instance.getPlayerManager().getSessionByPlayer(this);
		try {
			s.sendCommand(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean comparePassword(String proposedPassword) {
		return password.equals(proposedPassword);
	}

	public void giveResource(ResourceType r, int amount) {
		resources[r.ordinal()] += amount;
	}

	public void giveCityResource(ResourceType r, int amount) {
		resources[r.ordinal()] += amount;
	}

	public void takeResource(ResourceType r, int amount) {
		if (resources[r.ordinal()] >= amount) {
			resources[r.ordinal()] -= amount;
		}
	}

	public void maritimeTrade(ResourceType rGet, ResourceType rGive) {
		if (resources[rGive.ordinal()] >= 4) {
			resources[rGive.ordinal()] -= 4;
			resources[rGet.ordinal()]++;
		}
	}
}