package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.server.model.map.Intersection;

public class PlaceSettlementAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Intersection selectedIntersection = gsm.getSelectedIntersection();

		if (selectedIntersection != null) {
			return selectedIntersection.canBuild();
		}

		return false;
	}

	@Override
	public String getDescription() {
		return "Build Settlement";
	}

	@Override
	public void sendCommand() {
		// TODO send the command
		System.out.println(this);
	}

}
