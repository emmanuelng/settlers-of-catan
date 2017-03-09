package catan.settlers.network.server.commands;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;

public class RollDiceCommand implements ClientToServerCommand {

	private int gameId;
	
	public RollDiceCommand(int gameId) {
		this.gameId = gameId;
	}
	
	@Override
	public void execute(Session sender, Server server) {
		Game g = server.getGameManager().getGameById(gameId);
		if (server.getPlayerManager().getPlayerBySession(sender) == g.getPlayersManager().getCurPlayer()) {
			g.rollDice();
			
		}

	}

}
