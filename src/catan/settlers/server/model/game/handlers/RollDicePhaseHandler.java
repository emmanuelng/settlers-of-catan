package catan.settlers.server.model.game.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import catan.settlers.network.client.commands.game.DiscardCardsCommand;
import catan.settlers.network.client.commands.game.NormalDiceRollCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.WaitForSevenDiscardCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.GameBoardManager;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.SetOfOpponentMove;
import catan.settlers.server.model.SetOfOpponentMove.MoveType;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Village;
import catan.settlers.server.model.units.Village.VillageKind;

public class RollDicePhaseHandler {

	private Game game;

	private ArrayList<Player> participants;
	private GameBoardManager gameBoardManager;
	private int barbarianHordeCounter;
	private int redDie, yellowDie, eventDie;

	public RollDicePhaseHandler(Game game) {
		this.game = game;
	}

	/**
	 * Roll dice phase logic. During this phase, the current player must roll
	 * the dice. The other players must wait. Resources are produced at this
	 * step, and multiple events can occur as well.
	 */
	public void handle(Player sender, TurnData data) {
		updateDataFromGame();

		// TODO: Play alchemist card
		assignRandomValuesToDice();

		if (redDie + yellowDie == 7) {
			SetOfOpponentMove discardResourcesSet = buildDiscardResourcesSet();

			if (!discardResourcesSet.isEmpty()) {
				game.setCurSetOfOpponentMove(discardResourcesSet);
				askOtherPlayersToWait(discardResourcesSet);
				return;
			}

		} else {
			distributeResources();
		}

		/* ==== Event dice events ==== */

		if (eventDie < 4) {
			barbarianHordeCounter++;
			if (barbarianHordeCounter >= 7) {
				barbarianAttack();
			}
		} else if (eventDie == 4) {
			// yellow improvement check
			for (Player p : participants) {
				int lvl = p.getTradeLevel();
				if (lvl != 0) {
					if (lvl + 1 >= redDie) {
						// TODO: p.drawTradeCard();
					}
				}
			}
		} else if (eventDie == 5) {
			// blue
			for (Player p : participants) {
				int lvl = p.getTradeLevel();
				if (lvl != 0) {
					if (lvl + 1 >= redDie) {
						// TODO: p.drawPoliticsCard();
					}
				}
			}
		} else if (eventDie == 6) {
			// green
			for (Player p : participants) {
				int lvl = p.getTradeLevel();
				if (lvl != 0) {
					if (lvl + 1 >= redDie) {
						// TODO: p.drawScienceCard();
					}
				}
			}
		}
		game.setGamePhase(GamePhase.TURNPHASE);
	}

	/**
	 * Gets the required data from the game object such as the current player or
	 * the participants list
	 */
	private void updateDataFromGame() {
		this.participants = game.getParticipants();
		this.gameBoardManager = game.getGameBoardManager();
		this.barbarianHordeCounter = game.getBarbarianHordeCounter();
	}

	/**
	 * Generates random values for the dice and stores them in the Game class.
	 */
	private void assignRandomValuesToDice() {
		this.redDie = 6;// (int) (Math.ceil(Math.random() * 6));
		this.yellowDie = 1;// (int) (Math.ceil(Math.random() * 6));
		this.eventDie = (int) (Math.ceil(Math.random() * 6));

		game.setDice(redDie, yellowDie, eventDie);
	}

	/**
	 * Checks if there are players with more than seven resource cards. If a
	 * player is in this case, he/she is added to a SetOfOpponentMove. If no
	 * player needs to discard resources, an empty SetOfOpponentMove is returned
	 * (i.e. SetOfOpponentMove.isEmpty() == true)
	 */
	private SetOfOpponentMove buildDiscardResourcesSet() {
		SetOfOpponentMove set = new SetOfOpponentMove(MoveType.SEVEN_DISCARD_CARDS);
		for (Player p : participants) {
			int nbResourceCards = p.getNbResourceCards();
			if (nbResourceCards > 7) {
				p.sendCommand(new DiscardCardsCommand());
				set.waitForPlayer(p);
			}
		}
		return set;
	}

	/**
	 * Sends a command to indicate to the players that they must wait until all
	 * players with more than seven resource cards have discarded their
	 * resources.
	 */
	private void askOtherPlayersToWait(SetOfOpponentMove set) {
		int nbOfResponses = set.nbOfResponses();
		int nbOfPlayers = set.nbOfPlayers();
		WaitForSevenDiscardCommand cmd = new WaitForSevenDiscardCommand(nbOfResponses, nbOfPlayers);

		for (Player p : participants)
			if (!set.contains(p))
				p.sendCommand(cmd);
	}

	/**
	 * Gives resources to the players depending on the position of their
	 * villages and the current dice value. Sends an update command to the
	 * players.
	 */
	private void distributeResources() {
		HashSet<Player> playersWhoDrew = gameBoardManager.produceResources(redDie + yellowDie);
		for (Player p : participants) {
			p.sendCommand(new NormalDiceRollCommand(redDie, yellowDie));
			p.sendCommand(new UpdateResourcesCommand(p.getResources()));

			if (!playersWhoDrew.contains(p) && p.hasAqueduct()) {
				// TODO: select a card
			}
		}
	}

	/**
	 * Barbarian attack logic.
	 */
	private void barbarianAttack() {
		int barbarianStrength = 0;

		HashMap<Player, Integer> playerStrength = new HashMap<>();
		for (Player p : participants)
			playerStrength.put(p, 0);

		/*
		 * Checks all intersections. If city, +1 to barbarian strength. If
		 * active knight, +1/2/3 to player strength
		 */
		for (Intersection i : gameBoardManager.getBoard().getIntersections()) {
			IntersectionUnit unit = i.getUnit();
			if (unit instanceof Village) {
				if (((Village) unit).getKind() != VillageKind.SETTLEMENT) {
					barbarianStrength++;
				}
			} else if (unit instanceof Knight) {
				if (((Knight) unit).isActive()) {
					int current = 0;
					switch (((Knight) unit).getKnightType()) {
					case BASIC_KNIGHT:
						current = playerStrength.get(unit.getOwner());
						playerStrength.put(unit.getOwner(), current + 1);
						break;
					case STRONG_KNIGHT:
						current = playerStrength.get(unit.getOwner());
						playerStrength.put(unit.getOwner(), current + 2);
						break;
					case MIGHTY_KNIGHT:
						current = playerStrength.get(unit.getOwner());
						playerStrength.put(unit.getOwner(), current + 3);
						break;
					}
				}
			}
		}

		int totalStrength = 0, minStrength = Integer.MAX_VALUE, maxStrength = 0;
		HashSet<Player> strongestPlayers = new HashSet<>(), weakestPlayers = new HashSet<>();
		for (Player player : playerStrength.keySet()) {
			int curPlayerStrength = playerStrength.get(player);
			totalStrength += curPlayerStrength;

			if (curPlayerStrength > maxStrength) {
				strongestPlayers = new HashSet<>();
				strongestPlayers.add(player);
				maxStrength = curPlayerStrength;
			} else if (curPlayerStrength < minStrength) {
				weakestPlayers = new HashSet<>();
				weakestPlayers.add(player);
				minStrength = curPlayerStrength;
			} else if (curPlayerStrength == maxStrength) {
				strongestPlayers.add(player);
			} else if (curPlayerStrength == minStrength) {
				weakestPlayers.add(player);
			}
		}

		if (barbarianStrength > totalStrength) {
			// remove cities from weakest players
		} else {
			// top player gets VP or top players get prog cards
		}

	}

}
