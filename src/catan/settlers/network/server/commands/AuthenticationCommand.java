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
			if (server.authenticatePlayer(username, password, sender)) {
				server.writeToConsole("Received authentication request");
				sender.sendCommand(new AuthResultCommand(username, true));
			} else {
				sender.sendCommand(new AuthResultCommand(username, false));
			}
		} catch (IOException e) {
			// Ignore
		}

	}

}
