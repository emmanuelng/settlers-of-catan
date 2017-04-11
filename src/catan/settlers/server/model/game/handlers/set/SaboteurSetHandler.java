package catan.settlers.server.model.game.handlers.set;

import java.util.HashMap;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.DiscardCardsCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;

public class SaboteurSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = -8055962901420671593L;
	private Game game;
	private TurnData data;
	private SetOfOpponentMove currentSetOfOpponentMove;
	private HashMap<ResourceType, Integer> sevenResources;

	@Override
	public void handle(Game game, Player sender, TurnData data) {
		System.out.println("Hello");
		if (!contains(sender))
			return;

		this.game = game;
		this.data = data;

		updateDatafromGame();

		int nbSelectedResources = countSelectedResources(sevenResources);
		int nbResourcesToDiscard = (int) Math.floor(sender.getNbResourceCards() / 2);

		if (nbSelectedResources == nbResourcesToDiscard) {
			removeResources(sender, sevenResources);
			game.setCurSetOfOpponentMove(null);
			
			System.out.println("Wololololo");
			game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));
			return;
		}

		sender.sendCommand(new DiscardCardsCommand("The Saboteur destroys half of your resources"));
	}

	private void updateDatafromGame() {
		this.currentSetOfOpponentMove = game.getCurrentSetOfOpponentMove();
		game.getParticipants();
		this.sevenResources = data.getSevenResources();
	}

	private int countSelectedResources(HashMap<ResourceType, Integer> sevenResources) {
		if (sevenResources == null)
			return 0;

		int nbSelectedResources = 0;
		for (ResourceType rtype : ResourceType.values()) {
			nbSelectedResources += sevenResources.get(rtype);
		}
		return nbSelectedResources;
	}

	private void removeResources(Player sender, HashMap<ResourceType, Integer> sevenResources) {
		for (ResourceType rtype : ResourceType.values()) {
			sender.removeResource(rtype, sevenResources.get(rtype));
		}
		sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
		currentSetOfOpponentMove.playerResponded(sender);
	}

}
