package catan.settlers.client.model;

import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.map.Edge;

public class ClientModel {
	public static final ClientModel instance = new ClientModel();
	public static final int WINDOW_WIDTH = 1366;
	public static final int WINDOW_HEIGHT = 768;

	private String username;
	private NetworkManager networkManager;
	private int curGameId;

	private Intersection selectedIntersection;
	private Edge selectEdge;
	
	private String currentInGameMessage;

	private ClientModel() {
		this.networkManager = new NetworkManager();
		this.currentInGameMessage = null;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setCurGameId(int id) {
		curGameId = id;
	}

	public int getCurGameId() {
		return curGameId;
	}

	public void setCurrentIntersection(Intersection currentSelectedIntersection) {
		this.selectedIntersection = currentSelectedIntersection;
	}

	public Intersection getCurrentIntersection() {
		return selectedIntersection;
	}

	public void setCurrentEdge(Edge currentSelectedEdge) {
		this.selectEdge = currentSelectedEdge;
	}

	public Edge getCurrentEdge() {
		return selectEdge;
	}
	
	public void setCurrentInGameMessage(String message) {
		currentInGameMessage = message;
	}
	
	public String getCurrentInGameMassage() {
		return currentInGameMessage;
	}
}
