package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class RollDicePhaseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private String curPlayerUsername;

	public RollDicePhaseCommand(String curPlayerUsername) {
		this.curPlayerUsername = curPlayerUsername;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();

		if (curPlayerUsername.equals(ClientModel.instance.getUsername())) {
			gsm.setdBox("It's your turn!", "Click on the roll dice button to start your turn");
		} else {
			gsm.setdBox("It's " + curPlayerUsername + "'s turn!", "Please wait until he/she rolls the dice");
		}
	}
}
