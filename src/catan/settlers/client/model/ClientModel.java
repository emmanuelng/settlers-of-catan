package catan.settlers.client.model;

import java.io.IOException;

import catan.settlers.network.client.Client;
import catan.settlers.network.server.commands.ClientToServerCommand;

public class ClientModel {
	public static final ClientModel instance = new ClientModel();
	private Client client;
	
	private ClientModel(){
		// TODO
	}
	
	public void connect(String IP, int portNumber) throws IOException{
		//client = new Client(IP,portNumber);
		client = new Client("132.206.54.90",3000);
		client.connect();
	}
	
	public void sendCommand(ClientToServerCommand cmd) {
		try {
			client.sendCommand(cmd);
		} catch (IOException e) {
			// TODO Failed to send command
			System.out.println("Failed to send registration request: " + e.getMessage());
		}
	}
}
