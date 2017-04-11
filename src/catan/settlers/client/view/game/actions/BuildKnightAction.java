package catan.settlers.client.view.game.actions;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.BuildKnightCommand;
import catan.settlers.server.model.Player.ResourceType;

public class BuildKnightAction implements GameAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		HashMap<ResourceType, Integer> resources = gsm.getResources();

		if (gsm.getSelectedIntersection() != null) {
			if (gsm.getSelectedIntersection().connected(ClientModel.instance.getUsername())) {
				return resources.get(ResourceType.ORE) > 0 && resources.get(ResourceType.WOOL) > 0;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Hire Knight";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		nm.sendCommand(new BuildKnightCommand());
		ClientModel.instance.getGameStateManager().setSelectedIntersection(null);
	}

	@Override
	public String getSuccessMessage() {
		return "Costs 1 ore and 1 wool";
	}

	@Override
	public String getFailureMessage() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		HashMap<ResourceType, Integer> resources = gsm.getResources();

		if (gsm.getSelectedIntersection() == null) {
			return "Select an intersection";
		} else {

			if (resources.get(ResourceType.ORE) < 1)
				return "Missing 1 ore";

			if (resources.get(ResourceType.WOOL) < 1)
				return "Missing 1 wool";

			if (gsm.getSelectedIntersection().getUnit() == null)
				return "Cannot place a knight here";

			if (gsm.getSelectedIntersection().getUnit().isKnight())
				return "The intersection is already occupied";

			if (!gsm.getSelectedIntersection().connected(ClientModel.instance.getUsername())) {
				return "A knight should be connected by a road";
			}
		}
		return "";
	}

}
