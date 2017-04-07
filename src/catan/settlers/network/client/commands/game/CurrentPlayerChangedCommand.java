package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class CurrentPlayerChangedCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -1580384017679159705L;
	private String player;

	public CurrentPlayerChangedCommand(String player) {
		this.player = player;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		
		// Hide menus
		gsm.setShowTradeMenu(false);
		gsm.setShowSelectPlayerMenu(false);
		gsm.setShowCommodityMenu(false);
		gsm.setShowSelectResourceMenu(false);
		
		gsm.setCurrentPlayer(player);
	}

}
