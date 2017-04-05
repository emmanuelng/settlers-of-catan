package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class EndOfSevenDiscardPhase implements ServerToClientCommand {

	private static final long serialVersionUID = -2380883639269667931L;

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowSevenDiscardMenu(false);
		gsm.setdBox(null, null);
	}

}
