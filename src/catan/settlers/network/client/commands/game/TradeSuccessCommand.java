package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class TradeSuccessCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -2760467558593232252L;
	private String username;

	public TradeSuccessCommand(String username) {
		this.username = username;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowTradeMenu(false);
		gsm.setdBox("Trade success!", username + " accpted the offer");
	}

}
