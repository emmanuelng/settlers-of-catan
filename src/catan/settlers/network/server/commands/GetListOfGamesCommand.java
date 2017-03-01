package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.GameListResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class GetListOfGamesCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(Session sender, Server server) {
		try {
			int nbOfGames = server.getGameManager().getListOfGames().size();
			server.writeToConsole(
					"Client requested the list of games. " + nbOfGames + " games are currently in the system.");
			sender.sendCommand(new GameListResponseCommand(server.getGameManager().getListOfGames()));
		} catch (IOException e) {
			// Ignore
		}
	}

}