package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;

public class IncrementTradeCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -6796324295742880632L;
	private int gameId;
	private TurnData turnData;

	public IncrementTradeCommand() {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.turnData = new TurnData(TurnAction.TRADE_CITY_IMPROVEMENT);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);

		if (game.getGamePhase() != GamePhase.ROLLDICEPHASE)
			game.receiveResponse(sender.getCredentials(), turnData);
	}

}