package catan.settlers.network.server.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;
import catan.settlers.server.model.map.Hexagon;

public class SelectHexFeedbackCommand implements ClientToServerCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2075199989462913734L;

	private int gameId;
	private TurnData data;

	public SelectHexFeedbackCommand(Hexagon selected) {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		
		this.gameId = gsm.getGameId();
		this.data = new TurnData(TurnAction.HEX_SELECTED);
		data.setSelectedHex(selected, gsm.getBoard());
	}

	@Override
	public void execute(Session sender, Server server) {

		Game game = server.getGameManager().getGameById(gameId);

		game.receiveResponse(sender.getCredentials(), data);
	}

}
