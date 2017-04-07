package catan.settlers.server.model.game.handlers.set;

import java.util.ArrayList;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.cards.MasterMerchantCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class MasterMerchantSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = -6070798169700146130L;
	private ArrayList<String> playersWithMoreVPs;

	public MasterMerchantSetHandler(Game game, ArrayList<String> playersWithMoreVPs) {
		this.playersWithMoreVPs = playersWithMoreVPs;
	}

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (contains(sender)) {
			Player selectedPlayer = game.getPlayersManager().getPlayerByUsername(data.getSelectedPlayer());

			if (selectedPlayer != null) {
				ResourceType card1 = selectedPlayer.drawRandomResource();
				selectedPlayer.removeResource(card1, 1);
				sender.giveResource(card1, 1);

				ResourceType card2 = selectedPlayer.drawRandomResource();
				selectedPlayer.removeResource(card2, 1);
				sender.giveResource(card2, 1);

				sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
				selectedPlayer.sendCommand(new UpdateResourcesCommand(selectedPlayer.getResources()));

				playerResponded(sender.getCredentials());
			}
		}

		if (allPlayersResponded()) {
			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
		} else {
			sender.sendCommand(new MasterMerchantCommand(playersWithMoreVPs, game.getCurrentPlayer().getUsername()));
		}
	}

}
