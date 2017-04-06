package catan.settlers.network.server.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.game.TradeSuccessCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class PlayerTradeConfirmCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1963406279320893958L;
	private HashMap<ResourceType, Integer> give, get;
	private Player proposedPlayer;
	private int gameId;

	public PlayerTradeConfirmCommand(HashMap<ResourceType, Integer> give, HashMap<ResourceType, Integer> get,
			Player proposedPlayer) {
		this.give = give;
		this.get = get;
		this.proposedPlayer = proposedPlayer;
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		Player player = game.getCurrentPlayer();

		for (ResourceType s : give.keySet()) {
			player.removeResource(s, give.get(s));
			proposedPlayer.giveResource(s, give.get(s));
		}
		for (ResourceType t : get.keySet()) {
			proposedPlayer.removeResource(t, get.get(t));
			player.giveResource(t, get.get(t));
		}

		for (Player p : game.getParticipants()) {
			p.sendCommand(new UpdateResourcesCommand(p.getResources()));
			p.sendCommand(new TradeSuccessCommand(player.getUsername()));
		}
	}

}
