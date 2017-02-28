package catan.settlers.network.server.commands;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class CreateGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private String username;

	public CreateGameCommand(String username) {
		this.username = username;
	}

	@Override
	public void execute(Session sender, Server server) {
		server.writeToConsole("Received creating game request");
		server.getGameManager().createGame(username);

		int nbOfGames = server.getGameManager().getListOfGames().size();
		server.writeToConsole("There are now " + nbOfGames + " games in the system");
	}

}
