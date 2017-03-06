package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.CancelJoinGameResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;

public class CancelJoinGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	
	private int gameId;
	
	public CancelJoinGameCommand(int gameId) {
		this.gameId = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.getPlayersManager().removePlayer(sender.getPlayer());
		
		try {
			sender.sendCommand(new CancelJoinGameResponseCommand(server.getGameManager().getListOfGames()));
		} catch (IOException e) {
			// Ignore
		}
	}

}
