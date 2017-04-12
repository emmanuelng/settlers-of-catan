package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.FishResourceCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class FishDrawResourceSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = 5880172781987896617L;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (!contains(sender))
			return;

		ResourceType rtype = data.getSelectedResourceOrCommodity();
		
		if (rtype != null) {
			sender.giveResource(rtype, 1);
			sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
			
			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
			return;
		}
		
		sender.sendCommand(new FishResourceCommand(sender.getUsername()));
	}

}
