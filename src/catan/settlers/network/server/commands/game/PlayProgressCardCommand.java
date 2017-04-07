package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.ProgressCards.ProgressCardType;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;

public class PlayProgressCardCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 7482248634248023713L;
	private int gameId;
	private TurnData data;

	public PlayProgressCardCommand(ProgressCardType pCardType){
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.data = new TurnData(TurnAction.PROGRESS_CARD);
		
		data.setProgressCard(pCardType);
	}
	
	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.receiveResponse(sender.getCredentials(), data);
	}

}
