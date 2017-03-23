package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.server.commands.game.BuildRoadCommand;
import catan.settlers.server.model.map.Edge;

public class BuildRoadAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Edge selectedEdge = gsm.getSelectedEdge();

		if (selectedEdge != null) {
			return selectedEdge.canBuild(ClientModel.instance.getUsername());
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Build a road. Costs 1 brick and 1 lumber";
	}

	@Override
	public void perform() {
		ClientModel.instance.getNetworkManager().sendCommand(new BuildRoadCommand());
	}

}
