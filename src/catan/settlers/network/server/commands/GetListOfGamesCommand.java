package catan.settlers.network.server.commands;

import java.io.IOException;
import java.util.ArrayList;

import catan.settlers.network.client.commands.GameListResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;

public class GetListOfGamesCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -645983387941219671L;

	@Override
	public void execute(Session sender, Server server) {
		try {
			ArrayList<Game> publicGames = server.getGameManager().getListOfPublicGames();
			ArrayList<Game> savedGames = server.getGameManager().getSavedGames(sender.getCredentials());
			sender.sendCommand(new GameListResponseCommand(publicGames, savedGames));
		} catch (IOException e) {
			// Ignore
			e.printStackTrace();
		}
	}

}
