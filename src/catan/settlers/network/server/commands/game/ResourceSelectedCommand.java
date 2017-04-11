package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;

public class ResourceSelectedCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -4269315346881327027L;
	private int gameId;
	private TurnData data;

	public ResourceSelectedCommand(ResourceType rtype) {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();

		this.data = new TurnData(TurnAction.RESOURCE_SELECTED);
		data.setSelectedResource(rtype);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.receiveResponse(sender.getCredentials(), data);
	}

}
