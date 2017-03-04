package catan.settlers.client.model;

public class ClientModel {
	public static final ClientModel instance = new ClientModel();

	private String username;
	private NetworkManager networkManager;

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
}
