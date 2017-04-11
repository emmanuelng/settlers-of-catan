package catan.settlers.server.model.game.handlers.set;

import java.rmi.server.UID;
import java.util.HashSet;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.cards.WeddingCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class WeddingSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = -1275534602181867301L;
	private HashSet<Player> playerWhoRespondedOnce;
	private Player playerWhoPlayedCard;

	public WeddingSetHandler(Player sender) {
		this.playerWhoPlayedCard = sender;
		playerWhoRespondedOnce = new HashSet<>();
	}

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (!contains(sender))
			return;

		ResourceType selectedResource = data.getSelectedResourceOrCommodity();

		if (selectedResource != null) {
			playerWhoPlayedCard.giveResource(selectedResource, 1);
			sender.removeResource(selectedResource, 1);

			sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
			playerWhoPlayedCard.sendCommand(new UpdateResourcesCommand(playerWhoPlayedCard.getResources()));
		}

		boolean mustPlay = true;
		if (playerWhoRespondedOnce.contains(sender)) {
			playerResponded(sender);
			mustPlay = false;
		} else {
			playerWhoRespondedOnce.add(sender);
		}

		if (allPlayersResponded()) {
			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
		} else {
			sender.sendCommand(new WeddingCommand(playerWhoPlayedCard.getUsername(), mustPlay));
		}
	}

}
