package catan.settlers.client.view.game.actions;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.server.commands.game.BuildSettlementCommand;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Intersection;

public class PlaceSettlementAction implements GameAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		String username = ClientModel.instance.getUsername();
		GamePhase curGamePhase = gsm.getCurrentPhase();
		Intersection selectedIntersection = gsm.getSelectedIntersection();
		HashMap<ResourceType, Integer> resources = ClientModel.instance.getGameStateManager().getResources();

		if (selectedIntersection != null && resources != null) {
			boolean hasResources = false;
			if (resources.get(ResourceType.BRICK) > 0 && resources.get(ResourceType.GRAIN) > 0
					&& resources.get(ResourceType.LUMBER) > 0 && resources.get(ResourceType.WOOL) > 0) {
				hasResources = true;
			}
			return selectedIntersection.canBuild(username, curGamePhase) && hasResources;
		}

		return false;
	}

	@Override
	public String getDescription() {
		return "Build Settlement";
	}

	@Override
	public void perform() {
		ClientModel.instance.getNetworkManager().sendCommand(new BuildSettlementCommand());
		ClientModel.instance.getGameStateManager().setSelectedIntersection(null);
	}

	@Override
	public String getSuccessMessage() {
		return "Costs 1 brick, 1 lumber, 1 wool, 1 grain";
	}

	@Override
	public String getFailureMessage() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		String username = ClientModel.instance.getUsername();
		GamePhase curGamePhase = gsm.getCurrentPhase();
		Intersection selectedIntersection = gsm.getSelectedIntersection();
		HashMap<ResourceType, Integer> resources = ClientModel.instance.getGameStateManager().getResources();

		if (selectedIntersection != null && resources != null) {
			if (resources.get(ResourceType.BRICK) < 1)
				return "Missing 1 brick";

			if (resources.get(ResourceType.LUMBER) < 1)
				return "Missing 1 lumber";

			if (resources.get(ResourceType.WOOL) < 1)
				return "Missing 1 wool";

			if (resources.get(ResourceType.GRAIN) < 1)
				return "Missing 1 grain";

			if (!selectedIntersection.canBuild(username, curGamePhase))
				return "Cannot build here";

		} else {
			return "Select an intersection";
		}

		return "Cannot build a settlement";
	}

}
