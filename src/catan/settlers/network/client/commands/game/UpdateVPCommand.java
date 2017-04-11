package catan.settlers.network.client.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class UpdateVPCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 8426658371599645191L;
	private HashMap<String, Integer> victoryPoints;

	public UpdateVPCommand(HashMap<String, Integer> victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setVictoryPoints(victoryPoints);

	}

}
