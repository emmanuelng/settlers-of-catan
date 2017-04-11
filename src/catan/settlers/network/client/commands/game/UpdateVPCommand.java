package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class UpdateVPCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8426658371599645191L;
	private int VP;

	public UpdateVPCommand(int VP) {
		this.VP = VP;
	}

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setVictoryPoints(VP);

	}

}
