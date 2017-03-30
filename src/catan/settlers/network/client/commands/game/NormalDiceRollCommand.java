package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class NormalDiceRollCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private int redDie;
	private int yellowDie;

	public NormalDiceRollCommand(int redDie, int yellowDie) {
		this.redDie = redDie;
		this.yellowDie = yellowDie;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setdBox((redDie + yellowDie) + " was rolled", "The hexagons produce resources");
	}

}
