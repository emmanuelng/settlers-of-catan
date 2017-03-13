package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.network.client.commands.TurnResponseCommand;
import catan.settlers.network.client.commands.game.PlaceElmtsSetupPhaseCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.WaitForPlayerCommand;
import catan.settlers.network.server.Server;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Village;

public class Game implements Serializable {

	public static enum GamePhase {
		READYTOJOIN, SETUPPHASEONE, SETUPPHASETWO, TURNPHASEONE
	}

	public static final int MAX_NB_OF_PLAYERS = 2;
	private static final long serialVersionUID = 1L;

	private int id;
	private ArrayList<Player> participants;
	private GamePlayersManager gamePlayersManager;
	private GameBoardManager gameBoardManager;
	private Player currentPlayer;
	private GamePhase currentPhase;

	public Game(int id, Player owner) {
		this.id = id;
		this.participants = new ArrayList<>();

		this.gamePlayersManager = new GamePlayersManager(owner, participants, id);
		this.gameBoardManager = new GameBoardManager();
		this.currentPhase = GamePhase.READYTOJOIN;
	}

	public void startGame() {
		this.currentPhase = GamePhase.SETUPPHASEONE;
		this.currentPlayer = participants.get(0);

		for (Player p : participants) {
			if (p == currentPlayer) {
				p.sendCommand(new PlaceElmtsSetupPhaseCommand(true));
			} else {
				p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
			}
		}

	}

	public void receiveResponse(Player sender, TurnData data) {
		switch (currentPhase) {
		case SETUPPHASEONE:
			setupPhase(sender, data, true);
			break;
		case SETUPPHASETWO:
			setupPhase(sender, data, false);
			break;
		default:
			break;
		}

	}

	/* ============ Game phases logic ============ */

	private void setupPhase(Player sender, TurnData data, boolean isPhaseOne) {
		if (sender == currentPlayer) {
			// If no edge or intersection selected, reject
			if (data.getEdgeSelection() == null || data.getIntersectionSelection() == null) {
				currentPlayer.sendCommand(new TurnResponseCommand("Please select an edge and an intersection", false));
				return;
			}

			// Get the selected intersection and edge
			GameBoard board = gameBoardManager.getBoard();
			Intersection interSelect = board.getIntersectionById(data.getIntersectionSelection().getId());
			Edge edgeSelect = board.getEdgeById(data.getEdgeSelection().getId());

			if (edgeSelect.hasIntersection(interSelect) && edgeSelect.getOwner() == null && interSelect.canBuild()) {
				// Update the board and send it to all the players
				Village v = new Village(currentPlayer);
				interSelect.setUnit(v);
				edgeSelect.setOwner(currentPlayer);
				updateAllPlayers();

				if (!isPhaseOne) {
					// In second setup phase, give the corresponding resources
					// to the player
					ArrayList<Hexagon> drawFor = interSelect.getHexagons();
					for (Hexagon h : drawFor) {
						ResourceType r = terrainToResource(h.getType());
						if (r != null) {
							currentPlayer.giveResource(r, 1);
						}
					}
				}

				// Stopping condition for phase one
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
				
				// Get the next player
				if (isPhaseOne) {
					currentPlayer = nextPlayer();
				} else {
					currentPlayer = previousPlayer();
				}
				
				// Send commands to the players
				for (Player p : participants) {
					if (p == currentPlayer) {
						p.sendCommand(new PlaceElmtsSetupPhaseCommand(isPhaseOne));
					} else {
						p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
					}
				}

				// Stopping condition for phase two
				if (!isPhaseOne && currentPlayer == participants.get(0)) {
					currentPhase = GamePhase.TURNPHASEONE;
				}

			} else {
				String message = "";
				if (!edgeSelect.hasIntersection(interSelect)) {
					message = "The intersection and the edge must be adjacent";
				} else if (edgeSelect.getOwner() != null) {
					message = "You cannot build on an ocupied edge/intersection";
				} else {
					message = "You cannot build here";
				}
				currentPlayer.sendCommand(new TurnResponseCommand(message, false));
			}
		}
	}

	/* ============ End of game phases ============ */

	public int getGameId() {
		return id;
	}

	public Player nextPlayer() {
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

	public void prevPlayer() {
		int index = (participants.indexOf(currentPlayer) + (participants.size() - 1)) % participants.size();
		currentPlayer = participants.get(index);
	}

	public GamePlayersManager getPlayersManager() {
		return gamePlayersManager;
	}

	public GameBoardManager getGameBoardManager() {
		return gameBoardManager;
	}

	public void endGame() {
		Server.getInstance().getGameManager().removeGame(this);
	}

	public ResourceType terrainToResource(TerrainType t) {
		switch (t) {
		case FOREST:
			return ResourceType.LUMBER;
		case MOUNTAIN:
			return ResourceType.ORE;
		case PASTURE:
			return ResourceType.WOOL;
		case HILLS:
			return ResourceType.BRICK;
		case FIELD:
			return ResourceType.GRAIN;
		default:
			return null;
		}
	}

	private void updateAllPlayers() {
		for (Player p : participants) {
			p.sendCommand(new UpdateGameBoardCommand(gameBoardManager.getBoardDeepCopy()));
		}
	}
}
