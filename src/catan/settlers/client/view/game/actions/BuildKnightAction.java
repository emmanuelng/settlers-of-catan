package catan.settlers.client.view.game.actions;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.BuildKnightCommand;
import catan.settlers.server.model.Player.ResourceType;

public class BuildKnightAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		HashMap<ResourceType, Integer> resources = gsm.getResources();

		return resources.get(ResourceType.ORE) > 0 && resources.get(ResourceType.WOOL) > 0;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Hire Knight";
	}

	@Override
	public void perform() {
		System.out.println("Build Knight!");
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		nm.sendCommand(new BuildKnightCommand());
	}

}
