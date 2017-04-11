package catan.settlers.client.view.game.actions;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.server.commands.game.UpgradeVillageCommand;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Village;
import catan.settlers.server.model.units.Village.VillageKind;

public class UpgradeToCityAction implements GameAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Intersection selectedIntersection = gsm.getSelectedIntersection();
		HashMap<ResourceType, Integer> resources = ClientModel.instance.getGameStateManager().getResources();

		if (selectedIntersection != null && resources != null) {
			IntersectionUnit unit = selectedIntersection.getUnit();
			boolean hasResources = false;
			if (resources.get(ResourceType.GRAIN) >= 2 && resources.get(ResourceType.ORE) >= 3) {
				hasResources = true;
			}
			if (unit != null) {
				if (unit instanceof Village) {
					Village village = (Village) unit;
					if (village.getKind() == VillageKind.SETTLEMENT) {
						return hasResources;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Upgrade to city";
	}

	@Override
	public void perform() {
		ClientModel.instance.getNetworkManager().sendCommand(new UpgradeVillageCommand());
		ClientModel.instance.getGameStateManager().setSelectedIntersection(null);
	}

	@Override
	public String getSuccessMessage() {
		return "Costs 2 grain and 3 ore";
	}

	@Override
	public String getFailureMessage() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Intersection selectedIntersection = gsm.getSelectedIntersection();
		HashMap<ResourceType, Integer> resources = ClientModel.instance.getGameStateManager().getResources();

		if (selectedIntersection != null && resources != null) {
			IntersectionUnit unit = selectedIntersection.getUnit();
			if (unit instanceof Village) {
				Village village = (Village) unit;

				if (village.getKind() != VillageKind.SETTLEMENT)
					return "Only settlements can be upgraded";

				if (resources.get(ResourceType.GRAIN) < 2)
					return "Missing " + (resources.get(ResourceType.GRAIN) - 2) + " grain";

				if (resources.get(ResourceType.ORE) < 3)
					return "Missing " + (resources.get(ResourceType.ORE) - 3) + " ore";
			}
		}

		return "Select a settlement";
	}

}
