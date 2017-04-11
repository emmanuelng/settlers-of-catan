package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class NormalDiceRollCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 5999260766339502866L;
	private int redDie;
	private int yellowDie;
	private int eventDie;

	public NormalDiceRollCommand(int redDie, int yellowDie, int eventDie) {
		this.redDie = redDie;
		this.yellowDie = yellowDie;
		this.eventDie = eventDie;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setdBox((redDie + yellowDie) + " was rolled", "Red Die: "+ redDie + " Yellow Die: "+ yellowDie + " Event Die: "+ eventDie);
	}

}
