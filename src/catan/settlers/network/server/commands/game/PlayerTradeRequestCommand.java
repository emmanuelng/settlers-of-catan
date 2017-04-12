package catan.settlers.network.server.commands.game;

import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.game.ReceiveTradeRequestCommand;
import catan.settlers.network.client.commands.game.TradeFailureCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class PlayerTradeRequestCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1018194253318455816L;
	private HashMap<ResourceType, Integer> give, get;
	private int gameId;

	public PlayerTradeRequestCommand(HashMap<ResourceType, Integer> offer, HashMap<ResourceType, Integer> price) {
		this.give = offer;
		this.get = price;
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		Player player = game.getCurrentPlayer();

		ArrayList<Player> otherPlayers = game.getParticipants();
		otherPlayers.remove(player);

		boolean cmdSent = false;
		for (Player p : otherPlayers) {
			if (canTrade(p)) {
				cmdSent = true;
				p.sendCommand(new ReceiveTradeRequestCommand(give, get, player));
			}
		}

		if (!cmdSent)
			player.sendCommand(new TradeFailureCommand("Nobody has enough resources to trade with you."));

	}

	private boolean canTrade(Player p) {
		for (ResourceType rtype : get.keySet()) {
			if (p.getResourceAmount(rtype) < get.get(rtype))
				return false;
		}
		return true;
	}

}
