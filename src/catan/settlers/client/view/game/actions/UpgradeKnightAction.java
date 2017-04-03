package catan.settlers.client.view.game.actions;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.server.commands.game.UpgradeKnightCommand;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Knight.KnightType;

public class UpgradeKnightAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		HashMap<ResourceType, Integer> resources = gsm.getResources();
		Intersection myIntersection = gsm.getSelectedIntersection();

		if (myIntersection != null) {
			if (myIntersection.getUnit() != null) {
				if (myIntersection.getUnit().isKnight()
						&& ((Knight) myIntersection.getUnit()).getType() != KnightType.MIGHTY_KNIGHT) {
					return resources.get(ResourceType.GRAIN) > 0 && resources.get(ResourceType.WOOL) > 0;
				}
			}
		}
		return false;

	}

	@Override
	public String getDescription() {
		return "Upgrade your Knight.";
	}

	@Override
	public void perform() {
		ClientModel.instance.getNetworkManager().sendCommand(new UpgradeKnightCommand());
	}

}
