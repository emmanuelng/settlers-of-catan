package catan.settlers.network.server.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class SevenDiscardCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private TurnData data;
	private int gameId;

	public SevenDiscardCommand(HashMap<ResourceType, Integer> resources) {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.data = new TurnData(ClientModel.instance);
		this.data.setSevenDiscardResources(resources);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.receiveResponse(sender.getCredentials(), data);
	}

}
