package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.DisplaceKnightCommand;
import catan.settlers.server.model.map.Intersection;

public class DisplaceKnightAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		Intersection selectedIntersec = gsm.getSelectedIntersection();

		if (selectedIntersec != null)
			return gsm.getCanMoveKnightIntersecIds().contains(selectedIntersec.getId());

		return false;
	}

	@Override
	public String getDescription() {
		return "Displace knight here";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		nm.sendCommand(new DisplaceKnightCommand());
	}

}
