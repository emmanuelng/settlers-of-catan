package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class TurnResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -4079657466461652815L;
	private boolean success;
	private String message;

	public TurnResponseCommand(String message, boolean success) {
		this.message = message;
		this.success = success;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();

		gsm.setdBox("Success!", message);

		if (!success)
			gsm.setdBox("Failure", message);
	}

}
