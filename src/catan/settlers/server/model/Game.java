package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.network.client.commands.TurnResponseCommand;
import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.PlaceElmtsSetupPhaseCommand;
import catan.settlers.network.client.commands.game.UpdatePlayerResourcesCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.WaitForPlayerCommand;
import catan.settlers.network.server.Credentials;
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

	public static final int MAX_NB_OF_PLAYERS = 1;
	private static final long serialVersionUID = 1L;

	private int id;
	private ArrayList<Player> participants;
	private GamePlayersManager gamePlayersManager;
	private GameBoardManager gameBoardManager;
	private Player currentPlayer;
	private GamePhase currentPhase;

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
			if (p == currentPlayer) {
				p.sendCommand(new PlaceElmtsSetupPhaseCommand(true));
			} else {
				p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
			}
			p.sendCommand(new CurrentPlayerChangedCommand(currentPlayer.getUsername()));
		}

	}

	public void receiveResponse(Credentials credentials, TurnData data) {
		Player player = gamePlayersManager.getPlayerByCredentials(credentials);
		switch (currentPhase) {
		case SETUPPHASEONE:
			setupPhase(player, data, true);
			break;
		case SETUPPHASETWO:
			setupPhase(player, data, false);
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
			Intersection selectedIntersection = board.getIntersectionById(data.getIntersectionSelection().getId());
			Edge edgeSelect = board.getEdgeById(data.getEdgeSelection().getId());

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
						ResourceType r = terrainToResource(h.getType());
						if (r != null) {
							currentPlayer.giveResource(r, 1);
						}
					}

					currentPlayer.sendCommand(new UpdatePlayerResourcesCommand(currentPlayer.getResources()));
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

				// Stopping condition for phase two. Initialize turn phase one.
				if (!isPhaseOne && currentPlayer == participants.get(0)) {
					currentPhase = GamePhase.TURNPHASEONE;
					for (Player p : participants) {
						p.sendCommand(new TurnResponseCommand("Going to turn phase one", true));
					}
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

	/* ============ End of game phases ============ */

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

	public GamePhase getGamePhase() {
		return currentPhase;
	}

	private void updateAllPlayers() {
		for (Player p : participants) {
			p.sendCommand(new UpdateGameBoardCommand(gameBoardManager.getBoardDeepCopy()));
		}
	}
}
