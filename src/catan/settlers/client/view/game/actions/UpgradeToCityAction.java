package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Village;
import catan.settlers.server.model.units.Village.VillageKind;

public class UpgradeToCityAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Intersection selectedIntersection = gsm.getSelectedIntersection();

		if (selectedIntersection != null) {
			IntersectionUnit unit = selectedIntersection.getUnit();
			if (unit != null) {
				if (unit instanceof Village) {
					Village village = (Village) unit;
					if (village.getKind() == VillageKind.SETTLEMENT) {
						return true;
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
		System.out.println(this);
	}

}
