package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class BarbarianAttackCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8308295005031084849L;

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setdBox("Barbarian Attack!", "The land of Catan has been attacked!");
	}

}
