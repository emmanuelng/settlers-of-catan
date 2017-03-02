package catan.settlers.network.server.commands;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class CreateGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(Session sender, Server server) {
		server.writeToConsole("Received creating game request");
		server.getGameManager().createGame(sender.getPlayer().getUsername());

		int nbOfGames = server.getGameManager().getListOfGames().size();
		server.writeToConsole("There are now " + nbOfGames + " games in the system");
	}

}
