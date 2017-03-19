package catan.settlers.server.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class Player implements Serializable {

	public enum ResourceType {
		BRICK, GRAIN, LUMBER, ORE, WOOL, CLOTH, COIN, PAPER
	}

	private static final long serialVersionUID = 1L;
	private HashMap<ResourceType, Integer> resources;
	private Credentials credentials;

	public Player(Credentials credentials) {
		this.credentials = credentials;
		this.resources = new HashMap<>();

		for (ResourceType resType : ResourceType.values()) {
			resources.put(resType, 0);
		}
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void sendCommand(ServerToClientCommand cmd) {
		Session s = Server.instance.getAuthManager().getSessionByCredentials(credentials);
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
	
	public void removeResource(ResourceType r, int amount) {
		resources.remove(r, amount);
	}

	public String getUsername() {
		return credentials.getUsername();
	}

	public Session getSession() {
		return Server.getInstance().getAuthManager().getSessionByCredentials(credentials);
	}

	public HashMap<ResourceType, Integer> getResources() {
		HashMap<ResourceType, Integer> res = new HashMap<>();

		for (ResourceType type : resources.keySet()) {
			res.put(type, resources.get(type));
		}

		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			Player other = (Player) obj;
			return credentials.getUsername().equals(other.credentials.getUsername());
		}
		return false;
	}
}