package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class PlaceElmtsSetupPhaseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean isPhaseOne;

	public PlaceElmtsSetupPhaseCommand(boolean isPhaseOne) {
		this.isPhaseOne = isPhaseOne;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setdBox("Place your first settlement and road",
				"Select an edge and an intersection and click on \"End Turn\" to confirm");

		if (!isPhaseOne) {
			gsm.setdBox("Place your first city and second road",
					"Select an edge and an intersection and click on \"End Turn\" to confirm");
		}
	}
}
