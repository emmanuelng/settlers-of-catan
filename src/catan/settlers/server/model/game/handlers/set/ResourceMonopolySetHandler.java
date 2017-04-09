package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class ResourceMonopolySetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = -6366948815679162408L;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (contains(sender)) {
			ResourceType selectedResource = data.getSelectedResourceOrCommodity();
			for (Player player : game.getParticipants()) {
				if (player != sender) {
					if (player.removeResource(selectedResource, 2))
						sender.giveResource(selectedResource, 2);
					player.sendCommand(new UpdateResourcesCommand(player.getResources()));
				}
			}

			sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));

			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
		}
	}

}
