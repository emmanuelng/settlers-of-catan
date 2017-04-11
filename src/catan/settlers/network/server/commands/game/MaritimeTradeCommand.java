package catan.settlers.network.server.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.game.TradeFailureCommand;
import catan.settlers.network.client.commands.game.TradeSuccessCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.model.units.Port.PortKind;

public class MaritimeTradeCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -8330536435754498295L;
	private HashMap<ResourceType, Integer> give, get;
	private int gameid;

	public MaritimeTradeCommand(HashMap<ResourceType, Integer> give, HashMap<ResourceType, Integer> get) {
		this.give = give;
		this.get = get;
		this.gameid = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameid);
		Player player = game.getCurrentPlayer();

		if (canTradeWithBank(game, player)) {
			for (ResourceType rtype : give.keySet())
				player.removeResource(rtype, give.get(rtype));

			for (ResourceType rtype : get.keySet())
				player.giveResource(rtype, get.get(rtype));

			player.sendCommand(new UpdateResourcesCommand(player.getResources()));
			player.sendCommand(new TradeSuccessCommand());
		} else {
			player.sendCommand(new TradeFailureCommand());
		}
	}

	/**
	 * Checks multiple trade possibilities. If this method returns true, it
	 * guarantees that the trade is a valid one. We can thus give and remove
	 * resources without any additional checks.
	 */
	private boolean canTradeWithBank(Game game, Player player) {
		// Check for ports
		for (ResourceType rtype : give.keySet()) {
			// Specific resource port
			if (player.hasPortOfThisResource(rtype))
				if (give.get(rtype) == 2 && allOtherGiveResourcesAreEmpty(rtype))
					if (onlyOneResourceInGet())
						return true;

			// General port
			if (player.hasPort(PortKind.ALLPORT))
				if (give.get(rtype) == 3 && allOtherGiveResourcesAreEmpty(rtype))
					if (onlyOneResourceInGet())
						return true;
		}

		// Check for merchant (2:1)
		if (player == game.getGameBoardManager().getBoard().getMerchantOwner()) {
			TerrainType merchantHexType = game.getGameBoardManager().getBoard().getMerchantHex().getType();
			ResourceType resourceAdvantage = Hexagon.terrainToResource(merchantHexType);

			if (give.get(resourceAdvantage) == 2 && allOtherGiveResourcesAreEmpty(resourceAdvantage))
				if (onlyOneResourceInGet())
					return true;
		}

		// Check for Merchant fleet (2:1)
		for (ResourceType rtype : ResourceType.values())
			if (player.getTradeAtAdvantage().contains(rtype))
				if (give.get(rtype) == 2 && allOtherGiveResourcesAreEmpty(rtype))
					if (onlyOneResourceInGet())
						return true;

		// Check for Trading House (2:1)
		for (ResourceType rtype : ResourceType.values())
			if (player.getTradeLevel() >= 3)
				if (rtype == ResourceType.COIN || rtype == ResourceType.CLOTH || rtype == ResourceType.PAPER)
					if (give.get(rtype) == 2 && allOtherGiveResourcesAreEmpty(rtype))
						if (onlyOneResourceInGet())
							return true;

		return false;
	}

	private boolean allOtherGiveResourcesAreEmpty(ResourceType resource) {
		for (ResourceType rtype : give.keySet())
			if (rtype != resource && give.get(rtype) > 0)
				return false;
		return true;
	}

	private boolean onlyOneResourceInGet() {
		int nbSelectedResources = 0;
		for (ResourceType rtype : get.keySet()) {
			nbSelectedResources += get.get(rtype);
			if (nbSelectedResources > 1)
				return false;
		}
		return true;
	}

}
