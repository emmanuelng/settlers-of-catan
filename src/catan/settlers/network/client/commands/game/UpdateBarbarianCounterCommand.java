package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class UpdateBarbarianCounterCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5595446857378209044L;
	private int barbarianCounter;

	public UpdateBarbarianCounterCommand(int i) {
		barbarianCounter = i;
	}

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setBarbarianCounter(barbarianCounter);
	}

}
