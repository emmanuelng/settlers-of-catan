package catan.settlers.network.server.commands.game.cards;

import catan.settlers.client.model.ClientModel;
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
	private Hexagon selectedHex;
	private int gameId;
	private TurnData data;
	
	public SelectHexFeedbackCommand(Hexagon selected){
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();

		this.data = new TurnData(TurnAction.HEX_SELECTED);
		data.setSelectedHex(selectedHex);
	}
	
	@Override
	public void execute(Session sender, Server server) {
		
		Game game = server.getGameManager().getGameById(gameId);

		game.getCurrentPlayer().setCurrentSelectedHex(selectedHex);
	}

}
