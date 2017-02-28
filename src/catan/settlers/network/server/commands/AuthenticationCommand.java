package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.AuthResultCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class AuthenticationCommand implements ClientToServerCommand {
	
	private String username;
	private String password;

	public AuthenticationCommand(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void execute(Session sender, Server server) {
		try {
			sender.sendCommand(new AuthResultCommand(server.authenticatePlayer(username, password, sender)));
		} catch (IOException e) {
			// Ignore
		}
		
	}
	
}
