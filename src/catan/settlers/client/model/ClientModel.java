package catan.settlers.client.model;

import java.util.ArrayList;

public class ClientModel {
	public static final ClientModel instance = new ClientModel();
	public static final int WINDOW_WIDTH = 1366;
	public static final int WINDOW_HEIGHT = 768;

	private String username;
	private NetworkManager networkManager;
	private ArrayList<GameRepresentation> gameList;
	private ArrayList<ClientModelObserver> observers;

	private ClientModel() {
		networkManager = new NetworkManager();
		gameList = new ArrayList<>();
		observers = new ArrayList<>();
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

	public void updateGameList(ArrayList<GameRepresentation> games) {
		gameList = new ArrayList<>();
		for (GameRepresentation game : games) {
			gameList.add(game);
		}
		
		for (ClientModelObserver observer: observers) {
			observer.gameListUpdated(gameList);
		}
	}
	
	
}
