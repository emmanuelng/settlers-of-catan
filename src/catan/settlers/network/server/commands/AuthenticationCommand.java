package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.AuthenticationResponseCommand;
import catan.settlers.network.client.commands.AuthenticationResponseCommand.Status;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class AuthenticationCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -6040820176829673521L;
	private String username;
	private String password;

	public AuthenticationCommand(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void execute(Session sender, Server server) {
		try {
			Status status = server.getAuthManager().authenticate(username, password, sender);
			if (status == Status.SUCCESS) {
				server.writeToConsole("Player " + sender.getCredentials().getUsername() + " was authenticated");
			}
			sender.sendCommand(new AuthenticationResponseCommand(username, status));
		} catch (IOException e) {
			// Ignore
		}

	}

}
