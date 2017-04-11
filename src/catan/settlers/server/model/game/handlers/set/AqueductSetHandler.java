package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.AqueductCommand;
import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class AqueductSetHandler extends SetOfOpponentMove {

	/**
	 * 
	 */
	private static final long serialVersionUID = -335286973850262531L;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (!contains(sender))
			return;

		if (data.getSelectedResourceOrCommodity() != null) {
			ResourceType selectedResource = data.getSelectedResourceOrCommodity();
			sender.giveResource(selectedResource, 1);
			sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));

			playerResponded(sender.getCredentials());

			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
		} else {
			game.sendToAllPlayers(new AqueductCommand(sender.getUsername()));
		}
	}

}
