package catan.settlers.client.model;

import java.io.IOException;

import catan.settlers.network.client.Client;

public class ClientModel {
	public static final ClientModel instance = new ClientModel();
	private Client newClient;
	
	private ClientModel(){
		
	}
	
	public void connect(String IP, int portNumber) throws IOException{
		newClient = new Client(IP,portNumber);
		newClient.connect();
	}
}
