package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.server.commands.game.BuildWallCommand;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Village;
import catan.settlers.server.model.units.Village.VillageKind;

public class BuildWallAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Intersection intersection = gsm.getSelectedIntersection();

		if (intersection != null && intersection.getUnit().isVillage()) {
			Village iu = (Village) intersection.getUnit();
			if (iu.getKind() == VillageKind.SETTLEMENT) {
				return false;
			} else {
				return true;
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

}
