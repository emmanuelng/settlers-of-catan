package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class TradeFailureCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 3409600555164533527L;

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setTradeMenuMessage("Failure. This trade is invalid. Please try again.");
	}

}
