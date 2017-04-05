package catan.settlers.network.server.commands.game.progresscards;

import java.util.ArrayList;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player.ResourceType;

public class SelectResourceResponseCommand implements ClientToServerCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2422330373186573552L;
	private ArrayList<ResourceType> resources;

	public SelectResourceResponseCommand(ArrayList<ResourceType> resources){
		this.resources = resources;
	}
	

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(ClientModel.instance.getGameStateManager().getGameId());

		game.getCurrentPlayer().setCurrentSelectedResources(resources);
	}

}
