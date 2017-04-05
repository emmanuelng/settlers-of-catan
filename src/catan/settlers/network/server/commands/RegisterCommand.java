package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.RegistrationResultCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class RegisterCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 2747933579100384876L;
	private String username;
	private String password;

	public RegisterCommand(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void execute(Session sender, Server server) {
		try {
			if (server.getAuthManager().register(username, password)) {
				// User successfully registered
				server.writeToConsole("New user " + username + " registered");
				sender.sendCommand(new RegistrationResultCommand(true));
			} else {
				// Failure
				sender.sendCommand(new RegistrationResultCommand(false));
			}
		} catch (IOException e) {
			// Ignore
		}

	}
}
