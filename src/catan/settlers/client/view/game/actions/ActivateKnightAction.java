package catan.settlers.client.view.game.actions;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.ActivateKnightCommand;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;

public class ActivateKnightAction implements GameAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Intersection selectedIntersec = gsm.getSelectedIntersection();
		HashMap<ResourceType, Integer> resources = gsm.getResources();

		if (resources.get(ResourceType.GRAIN) < 1)
			return false;

		if (selectedIntersec != null) {
			IntersectionUnit unit = selectedIntersec.getUnit();
			if (unit != null) {
				if (unit instanceof Knight) {
					Knight knight = (Knight) unit;
					return !knight.isActive();
				}
			}
		}

		return false;
	}

	@Override
	public String getDescription() {
		return "Activate knight";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		nm.sendCommand(new ActivateKnightCommand());
	}

	@Override
	public String getSuccessMessage() {
		return "Costs 1 grain";
	}

	@Override
	public String getFailureMessage() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();

		if (gsm.getSelectedIntersection() == null) {
			return "Select an intersection";
		} else {
			if (!(gsm.getSelectedIntersection().getUnit() instanceof Knight))
				return "Select a knight";
		}

		if (gsm.getResources().get(ResourceType.GRAIN) < 1)
			return "Missing 1 grain";

		return "The knight is already activated";
	}

}
