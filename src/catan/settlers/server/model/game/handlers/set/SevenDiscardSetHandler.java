package catan.settlers.server.model.game.handlers.set;

import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.MoveRobberCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.WaitForSetOfOpponentMoveCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.game.handlers.MoveRobberHandler;

public class SevenDiscardSetHandler extends SetOfOpponentMove {

	private static final long serialVersionUID = -5769163759033347539L;
	private Game game;
	private TurnData data;
	private ArrayList<Player> participants;
	private HashMap<ResourceType, Integer> sevenResources;

	@Override
	public void handle(Game game, Player sender, TurnData data) {

		if (!contains(sender))
			return;

		this.game = game;
		this.data = data;

		updateDatafromGame();
		removeResources(sender, sevenResources);

		if (!allPlayersResponded()) {
			askOtherPlayersToWait();
		} else {
			endSevenDiscardPhase();
		}
	}

	private void updateDatafromGame() {
		this.participants = game.getParticipants();
		this.sevenResources = data.getSevenResources();
	}

	private void removeResources(Player sender, HashMap<ResourceType, Integer> sevenResources) {
		for (ResourceType rtype : ResourceType.values()) {
			sender.removeResource(rtype, sevenResources.get(rtype));
		}

		sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
		playerResponded(sender);
	}

	private void askOtherPlayersToWait() {
		int nbOfResponses = nbOfResponses();
		int nbOfPlayers = nbOfPlayers();
		WaitForSetOfOpponentMoveCommand cmd = new WaitForSetOfOpponentMoveCommand(nbOfResponses, nbOfPlayers,
				"SevenDiscard");

		for (Player p : participants)
			if (!contains(p))
				p.sendCommand(cmd);
	}

	private void endSevenDiscardPhase() {
		game.setCurSetOfOpponentMove(null);
		game.sendToAllPlayers(new CurrentPlayerChangedCommand(game.getCurrentPlayer().getUsername()));

		game.setGamePhase(GamePhase.TURNPHASE);
		if (game.getAttacked()) {
			MoveRobberHandler set = new MoveRobberHandler();
			set.waitForPlayer(game.getCurrentPlayer());
			game.setCurSetOfOpponentMove(set);
			game.getCurrentPlayer().sendCommand(new MoveRobberCommand(false));
		}

	}

}
