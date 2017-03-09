package catan.settlers.client.model;

import catan.settlers.client.view.game.IntersectionImage;
import catan.settlers.server.model.map.Intersection;

public class ClientModel {
	public static final ClientModel instance = new ClientModel();
	public static final int WINDOW_WIDTH = 1366;
	public static final int WINDOW_HEIGHT = 768;

	private String username;
	private NetworkManager networkManager;
	private int curGameId;
	private Intersection currentSelectedIntersection;

	private ClientModel() {
		networkManager = new NetworkManager();
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
	
	public void setCurrentIntersection(Intersection currentSelectedIntersection){
		this.currentSelectedIntersection=currentSelectedIntersection;
	}
	
	public Intersection getCurrentIntersection(){
		return currentSelectedIntersection;
	}
}
