package catan.settlers.client.view.game.actions;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.server.commands.game.BuildWallCommand;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Village;
import catan.settlers.server.model.units.Village.VillageKind;

public class BuildWallAction implements GameAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Intersection intersection = gsm.getSelectedIntersection();

		if (intersection != null) {
			if (intersection.getUnit() != null) {

				if (!intersection.getUnit().isVillage())
					return false;

				Village iu = (Village) intersection.getUnit();
				if (iu.getKind() == VillageKind.SETTLEMENT) {
					return false;
				} else {
					return !iu.hasWalls();
				}
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Build Wall";
	}

	@Override
	public void perform() {
		ClientModel.instance.getNetworkManager().sendCommand(new BuildWallCommand());
		ClientModel.instance.getGameStateManager().setSelectedIntersection(null);
	}

	@Override
	public String getSuccessMessage() {
		return "Costs 2 bricks";
	}

	@Override
	public String getFailureMessage() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		HashMap<ResourceType, Integer> resources = gsm.getResources();
		Intersection intersection = gsm.getSelectedIntersection();

		if (intersection != null) {
			if (intersection.getUnit() != null) {
				if (intersection.getUnit() != null) {

					if (!intersection.getUnit().isVillage())
						return "Select a city";

					Village iu = (Village) intersection.getUnit();
					if (iu.getKind() == VillageKind.SETTLEMENT) {
						return "Cannot build walls on settlements";
					} else {
						if (resources.get(ResourceType.BRICK) < 2)
							return "Missing " + (2 - resources.get(ResourceType.BRICK)) + "bricks";
						
						if (iu.hasWalls())
							return "This city has already walls";
					}
				}
			}
		}

		return "Select a city";
	}

}
