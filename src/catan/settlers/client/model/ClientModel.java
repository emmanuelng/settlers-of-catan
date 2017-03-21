package catan.settlers.client.model;

import catan.settlers.network.server.commands.JoinGameCommand;

public class ClientModel {

	public static final ClientModel instance = new ClientModel();

	private NetworkManager networkManager;
	private GameStateManager gameStateManager;
	private ImageFileManager imageFileManager;
	private ActionManager actionManager;

	private String username;

	private ClientModel() {
		this.networkManager = new NetworkManager();
		this.imageFileManager = new ImageFileManager();
		this.actionManager = new ActionManager();
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
	
	public ActionManager getActionManager() {
		return actionManager;
	}

	
	public void joinGame(int gameId) {
		networkManager.sendCommand(new JoinGameCommand(gameId));
	}

	public void joinGameSuccess(int gameId) {
		gameStateManager = new GameStateManager(gameId);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public ImageFileManager getImageFileManager() {
		return imageFileManager;
	}
}
