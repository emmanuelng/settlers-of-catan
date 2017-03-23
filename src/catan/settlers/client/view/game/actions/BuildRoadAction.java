package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.server.model.map.Edge;

public class BuildRoadAction implements Action {

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
		System.out.println(this);
	}

}
