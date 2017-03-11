package catan.settlers.network.client.commands.game;

import catan.settlers.network.client.commands.ServerToClientCommand;

public class WaitForPlayerCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private String username;

	public WaitForPlayerCommand(String username) {
		this.username = username;
	}

	@Override
	public void execute() {
		// TODO Display message on the client
		// Also disable buttons? Or have a myTurn state?
		System.out.println("Waiting for player " + username);
	}

}
