package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class TradeFailureCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 3409600555164533527L;
	private String message;

	public TradeFailureCommand() {
		this.message = "This trade is invalid. Please try again.";
	}

	public TradeFailureCommand(String message) {
		this.message = message;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setdBox("Trade failure", message);
	}

}
