package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class UpdateTradeMetOwnerCommand implements ServerToClientCommand{
	/**
	 * 
	 */
	private static final long serialVersionUID = -809352556813742623L;
	private String tradeMetOwner;

	public UpdateTradeMetOwnerCommand(String tradeMetOwner){
		this.tradeMetOwner = tradeMetOwner;
	}
	
	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setTradeMetOwner(tradeMetOwner);
	}
}
