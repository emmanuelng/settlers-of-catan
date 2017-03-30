package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.client.commands.TurnResponseCommand;
import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.DiscardCardsCommand;
import catan.settlers.network.client.commands.game.MoveRobberCommand;
import catan.settlers.network.client.commands.game.PlaceElmtsSetupPhaseCommand;
import catan.settlers.network.client.commands.game.RollDicePhaseCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.WaitForPlayerCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.SetOfOpponentMove.MoveType;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Village;
import catan.settlers.server.model.units.Village.VillageKind;

public class Game implements Serializable {

	public enum turnAction {
		BUILDSETTLEMENT, BUILDKNIGHT, BUILDROAD, UPGRADESETTLEMENT, UPGRADEKNIGHT, ENDTURN
	}

	public static enum GamePhase {
		READYTOJOIN, SETUPPHASEONE, SETUPPHASETWO, ROLLDICEPHASE, TURNPHASE
	}

	public static final int MAX_NB_OF_PLAYERS = 1;
	private static final long serialVersionUID = 1L;

	private int id;
	private ArrayList<Player> participants;
	private GamePlayersManager gamePlayersManager;
	private GameBoardManager gameBoardManager;
	private Player currentPlayer;
	private GamePhase currentPhase;
	private SetOfOpponentMove currentSetOfOpponentMove;

	private int redDie;
	private int yellowDie;
	private int eventDie;
	
	private int barbarianHordeCounter;

	public Game(int id, Credentials owner) {
		this.id = id;
		this.currentPhase = GamePhase.READYTOJOIN;
		this.participants = new ArrayList<>();

		this.gamePlayersManager = new GamePlayersManager(owner, participants, id);
		this.gameBoardManager = new GameBoardManager();
	}

	public void startGame() {
		this.currentPhase = GamePhase.SETUPPHASEONE;
		this.currentPlayer = participants.get(0);

		for (Player p : participants) {
			p.sendCommand(new CurrentPlayerChangedCommand(currentPlayer.getUsername()));
			if (p == currentPlayer) {
				p.sendCommand(new PlaceElmtsSetupPhaseCommand(true));
			} else {
				p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
			}
		}

	}

	public void receiveResponse(Credentials credentials, TurnData data) {
		Player player = gamePlayersManager.getPlayerByCredentials(credentials);

		/*
		 * Handle the current set of opponent move if any. Otherwise follow the
		 * normal game logic
		 */

		if (currentSetOfOpponentMove != null) {
			if (currentSetOfOpponentMove.contains(player)) {
				// TODO: handle the set of opponent move
			} else {
				// TODO: Ask to the player to wait
			}
		} else {
			switch (currentPhase) {
			case SETUPPHASEONE:
				setupPhase(player, data, true);
				break;
			case SETUPPHASETWO:
				setupPhase(player, data, false);
				break;
			case ROLLDICEPHASE:
				rollDicePhase(player, data);
				break;
			case TURNPHASE:
				turnPhase(player, data);
			default:
				break;
			}
		}

	}

	/* ======================== Game phases logic ======================== */

	private void setupPhase(Player sender, TurnData data, boolean isPhaseOne) {
		if (sender == currentPlayer) {
			// If no edge or intersection selected, reject
			if (data.getEdgeSelection() == null || data.getIntersectionSelection() == null) {
				String message = "Please select an edge and an intersection";
				currentPlayer.sendCommand(new TurnResponseCommand(message, false));
				return;
			}

			// Get the selected intersection and edge
			GameBoard board = gameBoardManager.getBoard();
			int intersectionId = data.getIntersectionSelection().getId();
			Intersection selectedIntersection = board.getIntersectionById(intersectionId);
			Edge edgeSelect = board.getEdgeById(data.getEdgeSelection().getId());

			// Check if the selected intersection and edge are valid
			if (edgeSelect.hasIntersection(selectedIntersection) && edgeSelect.getOwner() == null
					&& selectedIntersection.canBuild()) {
				// Update the board and send it to all the players
				Village v = new Village(currentPlayer);
				selectedIntersection.setUnit(v);
				edgeSelect.setOwner(currentPlayer);

				if (!isPhaseOne)
					v.upgradeToCity();

				updateAllPlayers();

				// In second setup phase, give resources to the player
				if (!isPhaseOne) {
					for (Hexagon h : selectedIntersection.getHexagons()) {
						ResourceType r = Hexagon.terrainToResource(h.getType());
						if (r != null) {
							currentPlayer.giveResource(r, 1);
						}
					}

					UpdateResourcesCommand cmd = new UpdateResourcesCommand(currentPlayer.getResources());
					currentPlayer.sendCommand(cmd);
				}

				// Stopping condition for phase one. Initialize setup phase two.
				if (isPhaseOne && currentPlayer == participants.get(participants.size() - 1)) {
					currentPhase = GamePhase.SETUPPHASETWO;
					for (Player p : participants) {
						if (p == currentPlayer) {
							p.sendCommand(new PlaceElmtsSetupPhaseCommand(false));
						} else {
							p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
						}
					}
					return;
				}

				// Stopping condition for phase two. Go to roll dice phase.
				if (!isPhaseOne && currentPlayer == participants.get(0)) {
					currentPhase = GamePhase.ROLLDICEPHASE;
					sendToAllPlayers(new RollDicePhaseCommand(currentPlayer.getUsername()));
					return;
				}

				// Get the next player
				Player new_player = isPhaseOne ? nextPlayer() : previousPlayer();
				setCurrentPlayer(new_player);

				// Send commands to the players
				for (Player p : participants) {
					if (p == currentPlayer) {
						p.sendCommand(new PlaceElmtsSetupPhaseCommand(isPhaseOne));
					} else {
						p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
					}
				}

			} else {
				String message = "You cannot build here";
				if (!edgeSelect.hasIntersection(selectedIntersection)) {
					message = "The intersection and the edge must be adjacent";
				} else if (edgeSelect.getOwner() != null) {
					message = "You cannot build on an ocupied edge/intersection";
				}
				currentPlayer.sendCommand(new TurnResponseCommand(message, false));
			}
		} else {
			sender.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
		}
	}

	private void rollDicePhase(Player sender, TurnData data) {
		// TODO: Play alchemist card

		/* ==== Assign random numbers to dice ==== */

		redDie = (int) (Math.ceil(Math.random() * 6));
		yellowDie = (int) (Math.ceil(Math.random() * 6));
		eventDie = (int) (Math.ceil(Math.random() * 6));

		/* ==== Red and yellow dice events ==== */

		// If a seven is rolled, discard cards and move the robber
		if (redDie + yellowDie == 7) {
			/*
			 * Count the number of resource cards for each player. Every player
			 * with more than 7 must select half of them and return them to the
			 * bank.
			 */
			SetOfOpponentMove set = new SetOfOpponentMove(MoveType.DISCARD_CARDS);
			for (Player p : participants) {
				int nbResourceCards = p.getNbResourceCards();
				if (nbResourceCards > 7) {
					p.sendCommand(new DiscardCardsCommand());
					set.waitForPlayer(p);
				}
			}
			if (!set.isEmpty())
				currentSetOfOpponentMove = set;

			/*
			 * Once all players have discarded their cards, ask the current
			 * player to move the robber
			 * 
			 * TODO: this must be allowed only after the first barbarian attack
			 */
			for (Player p : participants) {
				if (p == sender) {
					p.sendCommand(new MoveRobberCommand());
				} else {
					p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
				}
			}
		} else {
			// If the rolled number is not seven, produce the resources
			gameBoardManager.drawForRoll(redDie + yellowDie);
			for (Player p : participants) {
				p.sendCommand(new UpdateResourcesCommand(p.getResources()));
			}
		}

		/* ==== Event dice events ==== */

		if (eventDie < 4) {
			barbarianHordeCounter++;
			if (barbarianHordeCounter >= 7) {
				barbarianAttack();
			}
		} else if (eventDie == 4) {
			// yellow improvement check
		} else if (eventDie == 5) {
			// blue
		} else if (eventDie == 6) {
			// green
		}
		currentPhase = GamePhase.TURNPHASE;
	}

	private void turnPhase(Player sender, TurnData data) {
		switch (data.getAction()) {
		case BUILDSETTLEMENT:
			Intersection selected = gameBoardManager.getBoard().getIntersectionById(data.getIntersectionSelection().getId());
			if (selected.canBuild()) {
				if (sender.getResourceAmount(ResourceType.BRICK) > 0 && sender.getResourceAmount(ResourceType.GRAIN) > 0
						&& sender.getResourceAmount(ResourceType.WOOL) > 0
						&& sender.getResourceAmount(ResourceType.LUMBER) > 0) {
					Village v = new Village(sender);
					selected.setUnit(v);
					sender.removeResource(ResourceType.BRICK, 1);
					sender.removeResource(ResourceType.GRAIN, 1);
					sender.removeResource(ResourceType.WOOL, 1);
					sender.removeResource(ResourceType.LUMBER, 1);
				}
			}
			break;
		case BUILDROAD:
			data.getEdgeSelection().setOwner(sender);
			sender.removeResource(ResourceType.BRICK, 1);
			sender.removeResource(ResourceType.LUMBER, 1);
			break;
		case UPGRADESETTLEMENT: 
			IntersectionUnit village = gameBoardManager.getBoard().getIntersectionById(data.getIntersectionSelection().getId()).getUnit();
			if (village instanceof Village) {
				if (sender.getResourceAmount(ResourceType.ORE) >= 3 && sender.getResourceAmount(ResourceType.GRAIN) >= 2) {
					((Village) village).upgradeToCity();
					sender.removeResource(ResourceType.ORE, 3);
					sender.removeResource(ResourceType.GRAIN, 2);
				}
			}
			break;
		case BUILDKNIGHT: 
			Intersection newKnight = gameBoardManager.getBoard().getIntersectionById(data.getIntersectionSelection().getId());
			if (newKnight.getUnit() == null) {
				if (sender.getResourceAmount(ResourceType.ORE) >= 1 && sender.getResourceAmount(ResourceType.WOOL) >= 1 && newKnight.connected(sender)) {
					IntersectionUnit k = new Knight(sender);
					newKnight.setUnit(k);
					sender.removeResource(ResourceType.ORE, 1);
					sender.removeResource(ResourceType.WOOL, 1);
				}
			}
			break;
		case ENDTURN:
			currentPlayer = nextPlayer();
			break;
		default:

		}
	}

	private void barbarianAttack() {
		int barbarianStrength = 0;
		HashMap<Player, Integer> playerStrength = new HashMap<>();
		for (Player p : participants) {
			playerStrength.put(p, 0);
		}
		// Checks all intersections. If city, +1 to barbarian strength. If
		// active knight, +1/2/3 to player strength
		ArrayList<Intersection> intersections = gameBoardManager.getBoard().getIntersections();
		for (Intersection i : intersections) {
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
		int totalStrength = 0;
		for (Map.Entry<Player, Integer> entry : playerStrength.entrySet()) {
			totalStrength += entry.getValue();
		}
		if (barbarianStrength > totalStrength) {
			// remove cities from weakest players
		} else {
			// top player gets VP or top players get prog cards
		}

	}

	/* ======================== End of game phases ======================== */

	public int getGameId() {
		return id;
	}

	public void setCurrentPlayer(Player player) {
		if (participants.contains(player)) {
			currentPlayer = player;

			for (Player p : participants) {
				p.sendCommand(new CurrentPlayerChangedCommand(player.getUsername()));
			}
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	private Player nextPlayer() {
		int index = (participants.indexOf(currentPlayer) + 1) % participants.size();
		return participants.get(index);
	}

	private Player previousPlayer() {
		int index = participants.indexOf(currentPlayer) - 1;
		if (index < 0) {
			index = participants.size() - 1;
		}
		return participants.get(index);
	}

	public GamePlayersManager getPlayersManager() {
		return gamePlayersManager;
	}

	public GameBoardManager getGameBoardManager() {
		return gameBoardManager;
	}

	public GamePhase getGamePhase() {
		return currentPhase;
	}

	private void updateAllPlayers() {
		for (Player p : participants) {
			p.sendCommand(new UpdateGameBoardCommand(gameBoardManager.getBoardDeepCopy()));
		}
	}

	private void sendToAllPlayers(ServerToClientCommand cmd) {
		for (Player p : participants) {
			p.sendCommand(cmd);
		}
	}
}
