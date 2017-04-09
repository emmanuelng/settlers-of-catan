package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.cards.MerchantFleetCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class MerchantFleetSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = -4973617363963123095L;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (!contains(sender))
			return;

		if (data.getSelectedResourceOrCommodity() != null) {
			ResourceType selectedResource = data.getSelectedResourceOrCommodity();
			sender.setTradeAtAdvantage(selectedResource);

			game.sendToAllPlayers(new MerchantFleetCommand(sender.getUsername(), sender.getTradeAtAdvantage()));
			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
		} else {
			game.sendToAllPlayers(new MerchantFleetCommand(sender.getUsername()));
		}
	}

}
