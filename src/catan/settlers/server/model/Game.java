package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.network.client.commands.TurnResponseCommand;
import catan.settlers.network.client.commands.game.PlaceElmtsSetupPhaseOneCommand;
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

	int phaseCounter;

	public Game(int id, Player owner) {
		this.id = id;
		this.participants = new ArrayList<>();

		this.gamePlayersManager = new GamePlayersManager(owner, participants, id);
		this.gameBoardManager = new GameBoardManager();
		this.currentPhase = GamePhase.READYTOJOIN;
	}

	public void startGame() {
		this.currentPhase = GamePhase.SETUPPHASEONE;
		phaseCounter = 0;
		// Send Place settlement command to current player, wait command to the
		// others
		currentPlayer = participants.get((int) Math.random() * participants.size());

		for (Player p : participants) {
			if (p == currentPlayer) {
				p.sendCommand(new PlaceElmtsSetupPhaseOneCommand());
			} else {
				p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
			}
		}

	}

	public void receiveResponse(Player sender, TurnData data) {
		switch (currentPhase) {
		case SETUPPHASEONE:
			setupPhaseOne(sender, data);
			break;
		case SETUPPHASETWO:
			setupPhaseTwo(sender, data);
			break;
		default:
			break;
		}

	}

	/* ============ Game phases logic ============ */

	private void setupPhaseOne(Player sender, TurnData data) {
		if (sender == currentPlayer) {
			GameBoard board = gameBoardManager.getBoard();
			Intersection interSelect = board.getIntersectionById(data.getIntersectionSelection().getId());
			Edge edgeSelect = board.getEdgeById(data.getEdgeSelection().getId());

			if (edgeSelect.hasIntersection(interSelect) && edgeSelect.getOwner() == null && interSelect.canBuild()) {
				Village v = new Village(currentPlayer);
				interSelect.setUnit(v);
				edgeSelect.setOwner(currentPlayer);

				UpdateGameBoardCommand refresh = new UpdateGameBoardCommand(gameBoardManager.getBoard());
				currentPlayer.sendCommand(new TurnResponseCommand("You've placed a settlement and road!", true));

				if (isLastPlayer(currentPlayer)) {
					currentPhase = GamePhase.SETUPPHASETWO;
					phaseCounter = 0;
				} else {
					currentPlayer = nextPlayer();
					for (Player p : participants) {
						p.sendCommand(refresh);
						if (p == currentPlayer) {
							p.sendCommand(new PlaceElmtsSetupPhaseOneCommand());
						} else {
							p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
						}
					}
				}
			} else {
				currentPlayer.sendCommand(new TurnResponseCommand("Invalid selection. Try again.", false));
			}
		}
	}

	private void setupPhaseTwo(Player sender, TurnData data) {
		if (sender == currentPlayer) {
			Intersection interSelect = gameBoardManager.getBoard()
					.getIntersectionById(data.getIntersectionSelection().getId());
			Edge edgeSelect = gameBoardManager.getBoard().getEdgeById(data.getEdgeSelection().getId());
			if (edgeSelect.hasIntersection(interSelect) && edgeSelect.getOwner() == null && interSelect.canBuild()) {
				Village v = new Village(currentPlayer);
				v.upgradeToCity();
				interSelect.setUnit(v);
				edgeSelect.setOwner(currentPlayer);

				ArrayList<Hexagon> drawFor = interSelect.getHexagons();
				for (Hexagon h : drawFor) {
					ResourceType r = terrainToResource(h.getType());
					if (r != null) {
						currentPlayer.giveResource(r, 1);
					}
				}

				UpdateGameBoardCommand refresh = new UpdateGameBoardCommand(gameBoardManager.getBoard());
				for (Player p : participants) {
					p.sendCommand(refresh);
				}
				currentPlayer.sendCommand(new TurnResponseCommand("You've placed a settlement and road!", true));

				phaseCounter++;
				if (phaseCounter == participants.size()) {
					currentPhase = GamePhase.TURNPHASEONE;
					phaseCounter = 0;
				} else {
					prevPlayer();
					for (Player p : participants) {
						if (p == currentPlayer) {
							p.sendCommand(new PlaceElmtsSetupPhaseOneCommand());
						} else {
							p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
						}
					}
				}
			} else {
				currentPlayer.sendCommand(new TurnResponseCommand("Invalid selection. Try again.", false));
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

	public boolean isLastPlayer(Player p) {
		return participants.indexOf(p) == participants.size() - 1;
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

	private ResourceType terrainToResource(TerrainType t) {
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
}
