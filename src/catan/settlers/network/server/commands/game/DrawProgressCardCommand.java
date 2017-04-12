package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;

public class DrawProgressCardCommand implements ClientToServerCommand {

	private int gameId;
	private String type;
	private TurnData data;

	public DrawProgressCardCommand(String type) {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.type = type;
		this.data = new TurnData(TurnAction.DRAW_PROGRESS_CARD);
		ClientModel.instance.getGameStateManager().setShowSelectCardTypeMenu(false);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		data.setSelectedProgressCardType(type);
		game.receiveResponse(sender.getCredentials(), data);
	}

}
