package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;

public class UpgradeKnightCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private int gameId;
	private TurnData data;

	public UpgradeKnightCommand() {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.data = new TurnData(TurnAction.UPGRADE_KNIGHT);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		if (game.getGamePhase() == GamePhase.TURNPHASE) {
			game.receiveResponse(sender.getCredentials(), data);
		}

	}

}
