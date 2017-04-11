package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class DeclareVictorCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6480101875520737303L;
	private String winner;

	public DeclareVictorCommand(String winner) {
		this.winner = winner;
	}

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setdBox(winner + " has won", winner + " is the lord of catan");

	}

}
