package catan.settlers.network.server.commands;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;

public class PlayerReadyCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private int gameId;

	public PlayerReadyCommand(int gameId) {
		this.gameId = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.getPlayersManager().playerIsReady(sender.getPlayer());
		// TODO
	}

}
