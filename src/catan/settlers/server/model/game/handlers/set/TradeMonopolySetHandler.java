package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class TradeMonopolySetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = 2674450192895433808L;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (contains(sender)) {
			ResourceType selectedResource = data.getSelectedResourceOrCommodity();
			for (Player player : game.getParticipants()) {
				if (player != sender) {
					if (player.removeResource(selectedResource, 1))
						sender.giveResource(selectedResource, 1);
					player.sendCommand(new UpdateResourcesCommand(player.getResources()));
				}
			}

			sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));

			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
		}
	}

}
