package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
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
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setVictor(winner);
		gsm.setWinningScreen(true);
	}

}
