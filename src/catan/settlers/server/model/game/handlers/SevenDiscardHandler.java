package catan.settlers.server.model.game.handlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.network.client.commands.game.DiscardCardsCommand;
import catan.settlers.network.client.commands.game.EndOfSevenDiscardPhase;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.WaitForSevenDiscardCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.SetOfOpponentMove;
import catan.settlers.server.model.Game.GamePhase;

public class SevenDiscardHandler implements Serializable {

	private static final long serialVersionUID = -5079495830370304982L;

	private Game game;
	private SetOfOpponentMove currentSetOfOpponentMove;
	private ArrayList<Player> participants;

	public SevenDiscardHandler(Game game) {
		this.game = game;
	}

	public void handle(Player sender, HashMap<ResourceType, Integer> sevenResources) {
		updateDatafromGame();

		int nbSelectedResources = countSelectedResources(sevenResources);
		int nbResourcesToDiscard = (int) Math.floor(sender.getNbResourceCards() / 2);

		if (nbSelectedResources == nbResourcesToDiscard) {
			removeResources(sender, sevenResources);

			if (!currentSetOfOpponentMove.allPlayersResponded()) {
				askOtherPlayersToWait();
			} else {
				endSevenDiscardPhase();
			}
		} else {
			sender.sendCommand(new DiscardCardsCommand());
		}
	}

	private void askOtherPlayersToWait() {
		int nbOfResponses = currentSetOfOpponentMove.nbOfResponses();
		int nbOfPlayers = currentSetOfOpponentMove.nbOfPlayers();
		WaitForSevenDiscardCommand cmd = new WaitForSevenDiscardCommand(nbOfResponses, nbOfPlayers);

		for (Player p : participants)
			if (!currentSetOfOpponentMove.contains(p))
				p.sendCommand(cmd);
	}

	private void updateDatafromGame() {
		this.currentSetOfOpponentMove = game.getCurrentSetOfOpponentMove();
		this.participants = game.getParticipants();
	}

	private int countSelectedResources(HashMap<ResourceType, Integer> sevenResources) {
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

	private void endSevenDiscardPhase() {
		game.setCurSetOfOpponentMove(null);
		for (Player p : participants)
			p.sendCommand(new EndOfSevenDiscardPhase());

		// TODO: If the first barbarian attack happened, ask to the current
		// player to move the robber. Otherwise, go to normal turn phase
		game.setGamePhase(GamePhase.TURNPHASE);

	}
}
