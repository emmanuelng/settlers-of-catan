package catan.settlers.server.model.game.handlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import catan.settlers.network.client.commands.game.AqueductCommand;
import catan.settlers.network.client.commands.game.BarbarianAttackCommand;
import catan.settlers.network.client.commands.game.ChooseProgressCardCommand;
import catan.settlers.network.client.commands.game.DiscardCardsCommand;
import catan.settlers.network.client.commands.game.NormalDiceRollCommand;
import catan.settlers.network.client.commands.game.UpdateBarbarianCounterCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.UpdateVPCommand;
import catan.settlers.network.client.commands.game.WaitForSetOfOpponentMoveCommand;
import catan.settlers.network.client.commands.game.cards.MerchantFleetCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.game.handlers.set.AqueductSetHandler;
import catan.settlers.server.model.game.handlers.set.MerchantFleetSetHandler;
import catan.settlers.server.model.game.handlers.set.SetOfOpponentMove;
import catan.settlers.server.model.game.handlers.set.SevenDiscardSetHandler;
import catan.settlers.server.model.GameBoardManager;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.ProgressCards;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Village;
import catan.settlers.server.model.units.Village.VillageKind;

public class RollDicePhaseHandler implements Serializable {

	private static final long serialVersionUID = -4184975528982809977L;

	private Game game;

	private ArrayList<Player> participants;
	private GameBoardManager gameBoardManager;
	private ProgressCards ProgCards;
	private int barbarianHordeCounter;
	private int redDie, yellowDie, eventDie;

	public RollDicePhaseHandler(Game game) {
		this.game = game;
		this.ProgCards = game.getProgressCards();
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

		/*
		 * apparently you cannot move the robber until the first barbarian
		 * attack hits, or play the bishop card until the first attack
		 */
		if (redDie + yellowDie == 7) {
			SevenDiscardSetHandler discardResourcesSet = buildDiscardResourcesSet();

			if (!discardResourcesSet.isEmpty()) {
				game.setCurSetOfOpponentMove(discardResourcesSet);
				askOtherPlayersToWait(discardResourcesSet,"SevenDiscard");
				return;
			}

		} else {
			distributeResources();
		}

		/* ==== Event dice events ==== */

		if (eventDie < 4) {
			game.increaseBarbarianHordeCounter();
			System.out.println(""+ barbarianHordeCounter);
			game.sendToAllPlayers(new UpdateBarbarianCounterCommand(barbarianHordeCounter));
			if (barbarianHordeCounter >= 7) {
				barbarianAttack();
				game.resetBarbarianHordeCounter();
				game.sendToAllPlayers(new BarbarianAttackCommand());
			}
		} else if (eventDie == 4) {
			// yellow improvement check
			for (Player p : participants) {
				int lvl = p.getTradeLevel();
				if (lvl != 0) {
					if (lvl + 1 >= redDie) {
						p.giveProgressCard(ProgCards.drawTradeCard());
					}
				}
			}
		} else if (eventDie == 5) {
			// blue
			for (Player p : participants) {
				int lvl = p.getPoliticsLevel();
				if (lvl != 0) {
					if (lvl + 1 >= redDie) {
						p.giveProgressCard(ProgCards.drawPoliticsCard());
					}
				}
			}
		} else if (eventDie == 6) {
			// green
			for (Player p : participants) {
				int lvl = p.getScienceLevel();
				if (lvl != 0) {
					if (lvl + 1 >= redDie) {
						p.giveProgressCard(ProgCards.drawScienceCard());
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
		this.redDie = 1; //(int) (Math.ceil(Math.random() * 6));
		this.yellowDie = 1 ;//(int) (Math.ceil(Math.random() * 6));
		this.eventDie = 1 ;//(int) (Math.ceil(Math.random() * 6));

		game.setDice(redDie, yellowDie, eventDie);
	}

	/**
	 * Checks if there are players with more than seven resource cards. If a
	 * player is in this case, he/she is added to a SetOfOpponentMove. If no
	 * player needs to discard resources, an empty SetOfOpponentMove is returned
	 * (i.e. SetOfOpponentMove.isEmpty() == true)
	 */
	private SevenDiscardSetHandler buildDiscardResourcesSet() {
		SevenDiscardSetHandler set = new SevenDiscardSetHandler();
		int numThreshold = 7 + game.getCurrentPlayer().getNumberOfWalls() * 2;
		for (Player p : participants) {
			int nbResourceCards = p.getNbResourceCards();
			if (nbResourceCards > numThreshold) {
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
	private void askOtherPlayersToWait(SetOfOpponentMove set, String reason) {
		int nbOfResponses = set.nbOfResponses();
		int nbOfPlayers = set.nbOfPlayers();
		WaitForSetOfOpponentMoveCommand cmd = new WaitForSetOfOpponentMoveCommand(nbOfResponses, nbOfPlayers, reason);

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

			if (!playersWhoDrew.contains(p) && p.hasAqueduct()) {
				// TODO: select a card
				AqueductSetHandler set = new AqueductSetHandler();
				set.waitForPlayer(p);
				game.setCurSetOfOpponentMove(set);

				p.sendCommand(new AqueductCommand(p.getUsername()));
				p.sendCommand(new NormalDiceRollCommand(redDie, yellowDie));
			}else{
				p.sendCommand(new NormalDiceRollCommand(redDie, yellowDie));
			}
			p.sendCommand(new UpdateResourcesCommand(p.getResources()));
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
					switch (((Knight) unit).getType()) {
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
		// Weakest players lose a random city
		if (barbarianStrength > totalStrength) {
			for (Player p : weakestPlayers) {
				ArrayList<Village> cities = new ArrayList<>();
				for (Intersection i : game.getGameBoardManager().getBoard().getIntersections()) {
					IntersectionUnit u = i.getUnit();
					if (u instanceof Village && u.getOwner() == p) {
						if (((Village) u).getKind() == VillageKind.CITY) {
							cities.add((Village) u);
						}
					}
				}
				if (cities.size() != 0) {
					Village v = cities.remove((int)Math.random()*cities.size());
					v.destroyCity();
					p.decrementVP(1);
					p.sendCommand(new UpdateVPCommand(game.getCurrentPlayer().getVP()));
				}
			}
		} else {
			// top player gets VP or top players get prog cards
			if (strongestPlayers.size() == 1) {
				for (Player p : strongestPlayers) {
					p.incrementVP(1);
				}
			} else {
				for (Player p : strongestPlayers) {
					p.sendCommand(new ChooseProgressCardCommand());
				}
			}
		}
		game.sendToAllPlayers(new UpdateGameBoardCommand(gameBoardManager.getBoardDeepCopy()));
	}

}
