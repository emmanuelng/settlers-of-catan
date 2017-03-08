package catan.settlers.network.server.commands;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;

public class StartGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private int gameId;
	
	public StartGameCommand(int gameId) {
		this.gameId = gameId;
	}
	
	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.setPhase(GamePhase.SETUPPHASEONE);
	}

}
