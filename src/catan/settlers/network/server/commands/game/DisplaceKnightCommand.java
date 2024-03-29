package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;

public class DisplaceKnightCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 6820223453746188728L;
	private int gameId;
	private TurnData data;

	public DisplaceKnightCommand() {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.data = new TurnData(TurnAction.DISPLACE_KNIGHT);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);

		if (game.getGamePhase() == GamePhase.TURNPHASE)
			game.receiveResponse(sender.getCredentials(), data);
	}

}
