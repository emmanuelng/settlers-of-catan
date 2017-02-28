package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.RegistrationResultCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class RegisterCommand implements ClientToServerCommand {
	private String username;
	private String password;

	public RegisterCommand(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void execute(Session sender, Server server) {
		try {
			sender.sendCommand(new RegistrationResultCommand(server.registerPlayer(username, password)));
		} catch (IOException e) {
			// Ignore
		}
		
	}
}
