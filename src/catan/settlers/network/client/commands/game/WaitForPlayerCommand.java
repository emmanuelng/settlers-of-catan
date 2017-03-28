package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class WaitForPlayerCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private String username;

	public WaitForPlayerCommand(String username) {
		this.username = username;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setdBox("Waiting for " + username, "Please wait until the turn is done");
	}

}
