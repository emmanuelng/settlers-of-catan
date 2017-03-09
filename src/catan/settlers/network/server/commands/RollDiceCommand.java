package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.RollDiceResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;

public class RollDiceCommand implements ClientToServerCommand {
	
	private static final long serialVersionUID = 1L;
	private int gameId;
	
	public RollDiceCommand(int gameId) {
		this.gameId = gameId;
	}
	
	@Override
	public void execute(Session sender, Server server) {
		Game g = server.getGameManager().getGameById(gameId);
		if (server.getPlayerManager().getPlayerBySession(sender) == g.getPlayersManager().getCurPlayer()) {
			g.rollDice();
			try {
				sender.sendCommand(new RollDiceResponseCommand(g.getRedDie(),g.getYellowDie()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
