package catan.settlers.network.server.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.TurnData.TurnAction;

public class DiscardCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 2910353679944890130L;
	private TurnData data;
	private int gameId;

	public DiscardCommand(HashMap<ResourceType, Integer> resources) {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
		this.data = new TurnData(TurnAction.SEVEN_DISCARD);
		this.data.setSevenDiscardResources(resources);
		ClientModel.instance.getGameStateManager().setShowSevenDiscardMenu(false);
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.receiveResponse(sender.getCredentials(), data);
	}

}
