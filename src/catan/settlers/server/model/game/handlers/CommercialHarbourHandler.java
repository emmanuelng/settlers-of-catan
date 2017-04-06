package catan.settlers.server.model.game.handlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.network.client.commands.game.DiscardCardsCommand;
import catan.settlers.network.client.commands.game.EndOfSevenDiscardPhase;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.WaitForSetOfOpponentMoveCommand;
import catan.settlers.network.client.commands.game.WaitForSevenDiscardCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.SetOfOpponentMove;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.Player.ResourceType;

public class CommercialHarbourHandler implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2758143592611701189L;

	private Game game;
	private SetOfOpponentMove currentSetOfOpponentMove;
	private ArrayList<Player> participants;

	public CommercialHarbourHandler(Game game) {
		this.game = game;
	}

	public void handle(Player sender) {
		updateDatafromGame();


		if (!currentSetOfOpponentMove.allPlayersResponded()) {
			askOtherPlayersToWait();
		} else {
			endSevenDiscardPhase();
		}
	
	}

	private void askOtherPlayersToWait() {
		int nbOfResponses = currentSetOfOpponentMove.nbOfResponses();
		int nbOfPlayers = currentSetOfOpponentMove.nbOfPlayers();
		WaitForSetOfOpponentMoveCommand cmd = new WaitForSetOfOpponentMoveCommand(nbOfResponses, nbOfPlayers, "CommercialHarbour");

		for (Player p : participants)
			if (!currentSetOfOpponentMove.contains(p))
				p.sendCommand(cmd);
	}

	private void updateDatafromGame() {
		this.currentSetOfOpponentMove = game.getCurrentSetOfOpponentMove();
		this.participants = game.getParticipants();
	}

	
}
