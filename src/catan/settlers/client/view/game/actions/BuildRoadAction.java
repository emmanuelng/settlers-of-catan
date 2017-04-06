package catan.settlers.client.view.game.actions;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.server.commands.game.BuildRoadCommand;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Edge;

public class BuildRoadAction implements GameAction {

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
		return "Build a road";
	}

	@Override
	public void perform() {
		ClientModel.instance.getNetworkManager().sendCommand(new BuildRoadCommand());
		ClientModel.instance.getGameStateManager().setSelectedEdge(null);
	}

	@Override
	public String getSuccessMessage() {
		return "Costs 1 brick and 1 lumber";
	}

	@Override
	public String getFailureMessage() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		HashMap<ResourceType, Integer> resources = gsm.getResources();
		Edge selectedEdge = gsm.getSelectedEdge();

		if (selectedEdge != null) {
			if (!selectedEdge.canBuild(ClientModel.instance.getUsername()))
				return "Cannot build a road here";

			if (resources.get(ResourceType.BRICK) < 1)
				return "Missing 1 brick";

			if (resources.get(ResourceType.LUMBER) < 1)
				return "Missing 1 lumber";
		} else {
			return "Select an edge";
		}

		return "Cannot build a road";
	}

}
