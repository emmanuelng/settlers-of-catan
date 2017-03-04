package catan.settlers.client.model;

import java.io.IOException;

import catan.settlers.network.client.Client;
import catan.settlers.network.server.commands.ClientToServerCommand;

public class NetworkManager {
	
	private Client client;
	
	public void connect(String IP, int portNumber) throws IOException {
		client = new Client(IP, portNumber);
		client.connect();
	}

	public void sendCommand(ClientToServerCommand cmd) {
		try {
			client.sendCommand(cmd);
		} catch (IOException e) {
			// ignore
		}
	}
}
