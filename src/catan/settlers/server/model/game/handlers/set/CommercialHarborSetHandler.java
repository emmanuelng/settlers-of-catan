package catan.settlers.server.model.game.handlers.set;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.cards.CommercialHarborCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class CommercialHarborSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = 7966810225794046174L;
	private Player playerWhoSelectsResources;
	private ResourceType selectedResource;
	private Player currentOpponent;

	public CommercialHarborSetHandler(Game game, Player playerWhoSelectsResources) {
		this.playerWhoSelectsResources = playerWhoSelectsResources;
		this.currentOpponent = game.getParticipants().get(0);
	}

	@Override
	public void handle(Game game, Player sender, TurnData data) {

		if (sender == playerWhoSelectsResources && selectedResource == null) {
			// Response from the person who gives resources
			this.selectedResource = data.getSelectedResourceOrCommodity();

			for (int i = game.getParticipants().indexOf(currentOpponent); i < game.getParticipants().size(); i++) {
				Player currentPlayer = game.getParticipants().get(i);
				if (contains(currentPlayer)) {
					currentOpponent = currentPlayer;
					break;
				}
			}
		} else if (contains(sender)) {
			// Response from a person who gives commodities
			ResourceType selectedCommodity = data.getSelectedResourceOrCommodity();

			playerWhoSelectsResources.giveResource(selectedCommodity, 1);
			sender.removeResource(selectedCommodity, 1);

			sender.giveResource(selectedResource, 1);
			playerWhoSelectsResources.removeResource(selectedResource, 1);

			sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
			playerWhoSelectsResources.sendCommand(new UpdateResourcesCommand(playerWhoSelectsResources.getResources()));
			
			playerResponded(sender.getCredentials());
			
			this.selectedResource = null;
		}

		if (allPlayersResponded()) {
			// End of move
			game.setCurSetOfOpponentMove(null);
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
		} else {
			if (selectedResource == null) {
				// Phase 1
				game.sendToAllPlayers(new CommercialHarborCommand(playerWhoSelectsResources.getUsername(),
						currentOpponent.getUsername()));
			} else {
				// Phase 2
				game.sendToAllPlayers(new CommercialHarborCommand(playerWhoSelectsResources.getUsername(),
						currentOpponent.getUsername(), selectedResource));
			}
		}
	}

}
