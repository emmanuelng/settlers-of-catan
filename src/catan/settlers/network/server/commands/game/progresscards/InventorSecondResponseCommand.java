package catan.settlers.network.server.commands.game.progresscards;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.map.Hexagon;

public class InventorSecondResponseCommand implements ClientToServerCommand {

	
	private static final long serialVersionUID = 1L;
	private Hexagon secondSelected;
	private int gameId;

	public InventorSecondResponseCommand(Hexagon secondSelected, int gameId){
		this.secondSelected = secondSelected;
		this.gameId = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		server.getGameManager().getGameById(gameId).setInventorSecondValue(secondSelected);
		server.getGameManager().getGameById(gameId).inventorCard();
	}

}
