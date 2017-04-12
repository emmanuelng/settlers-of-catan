package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;
import catan.settlers.server.model.game.handlers.FishHandler.FishAction;

public class FishActionCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -6819252306466047985L;
	private int gameId;
	private TurnData turnData;

	public FishActionCommand(FishAction action) {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.turnData = new TurnData(TurnAction.FISH_ACTION);
		turnData.setFishAction(action);
		ClientModel.instance.getGameStateManager().setShowFishMenu(false);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);

		if (game.getGamePhase() == GamePhase.TURNPHASE)
			game.receiveResponse(sender.getCredentials(), turnData);
	}

}
