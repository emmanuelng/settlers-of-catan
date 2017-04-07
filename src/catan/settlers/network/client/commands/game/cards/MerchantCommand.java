package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class MerchantCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7503047052809320929L;
	private String currentPlayerUsername;
	private Object selectedHex;

	public MerchantCommand(String curPlayerUsername) {
		this.currentPlayerUsername = curPlayerUsername;
		this.selectedHex = null;
	}
	
	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);
		
		if (currentPlayerUsername.equals(ClientModel.instance.getUsername()) && selectedHex == null) 
			gsm.setdBox("You played a merchant card!", "Please select the hex you wish to place your merchant");
			gsm.doShowSelectHexLayer(true);

	}

}
