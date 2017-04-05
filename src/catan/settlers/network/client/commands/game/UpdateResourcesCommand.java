package catan.settlers.network.client.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Player.ResourceType;

public class UpdateResourcesCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -1766913399328566064L;
	private HashMap<ResourceType, Integer> resources;

	public UpdateResourcesCommand(HashMap<ResourceType, Integer> resources) {
		this.resources = resources;
	}

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setResources(resources);
	}

}
