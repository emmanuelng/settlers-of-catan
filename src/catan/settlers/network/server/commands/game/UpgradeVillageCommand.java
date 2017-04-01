package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.Game.turnAction;
import catan.settlers.server.model.TurnData;

public class UpgradeVillageCommand implements ClientToServerCommand {

	
	private static final long serialVersionUID = 1L;
	private int gameId;
	private TurnData turnData;

	public UpgradeVillageCommand(){
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.turnData = new TurnData(ClientModel.instance);
		turnData.setAction(turnAction.UPGRADESETTLEMENT);
	}
	
	@Override
	public void execute(Session sender, Server server) {
		// TODO Auto-generated method stub
		Game game = server.getGameManager().getGameById(gameId);
		
		if (game.getGamePhase() != GamePhase.ROLLDICEPHASE)
			game.receiveResponse(sender.getCredentials(), turnData);
	}

}
