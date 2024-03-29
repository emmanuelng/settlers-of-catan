package catan.settlers.network.server.commands.game.cards;

import java.io.IOException;

import catan.settlers.network.client.commands.game.cards.InventorSecondCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.map.Hexagon;

public class InventorFirstResponseCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1456865646989261416L;
	private Hexagon firstSelected;
	private int gameId;

	public InventorFirstResponseCommand(Hexagon firstSelected, int gameId) {
		this.firstSelected = firstSelected;
		this.gameId = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		try {
			server.getGameManager().getGameById(gameId).setInventorFirstHex(firstSelected);
			sender.sendCommand(new InventorSecondCommand());
		} catch (IOException e) {

		}
	}

}
