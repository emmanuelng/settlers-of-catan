package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;

public class SelectIntersectionFeedbackCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 5794128706772207762L;
	private int gameId;
	private TurnData data;

	public SelectIntersectionFeedbackCommand() {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.data = new TurnData(TurnAction.SELECT_INTERSECTION);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.receiveResponse(sender.getCredentials(), data);
		;
	}

}
