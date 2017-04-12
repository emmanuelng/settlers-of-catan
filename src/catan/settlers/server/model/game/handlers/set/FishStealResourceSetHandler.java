package catan.settlers.server.model.game.handlers.set;

import java.util.ArrayList;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.fish.FishStealResourceCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class FishStealResourceSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = -4161959739271435970L;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		if (!contains(sender))
			return;

		String selectedPlayerUsername = data.getSelectedPlayer();
		Player selectedPlayer = game.getPlayersManager().getPlayerByUsername(selectedPlayerUsername);

		if (selectedPlayer != null) {
			ResourceType rtype = selectedPlayer.drawRandomResource();
			if (rtype != null) {
				selectedPlayer.removeResource(rtype, 1);
				sender.giveResource(rtype, 1);

				selectedPlayer.sendCommand(new UpdateResourcesCommand(selectedPlayer.getResources()));
				sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));

				game.setCurSetOfOpponentMove(null);
				game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
				return;
			}
		}

		ArrayList<String> choosablePlayers = game.getPlayersManager().getParticipantsUsernames();
		choosablePlayers.remove(sender.getUsername());
		sender.sendCommand(new FishStealResourceCommand(sender.getUsername(), choosablePlayers));
	}

}
