package catan.settlers.network.client.commands.game.progresscards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class SelectResourceCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9198493949651455903L;
	private String reason;

	public SelectResourceCommand(String reason){
		this.reason = reason;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		int gameId = gsm.getGameId();
		gsm.doShowSelectResourceMenu();
		gsm.setShowSelectResourceMenuReason(reason);
		
	}

}
