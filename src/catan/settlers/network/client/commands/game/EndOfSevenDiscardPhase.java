package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class EndOfSevenDiscardPhase implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setShowSevenDiscardMenu(false);
	}

}
