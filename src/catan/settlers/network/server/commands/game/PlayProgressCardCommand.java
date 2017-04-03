package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayProgressCardCommand implements ClientToServerCommand {

	private int gameId;
	private ProgressCardType pCardType;
	private static final long serialVersionUID = 1L;

	public PlayProgressCardCommand(ProgressCardType pCardType){
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.pCardType = pCardType;
	}
	
	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.getCurrentPlayer().useProgressCard(pCardType);
	}

}