package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.DiscardCardsCommand;
import catan.settlers.network.client.commands.game.EndOfSevenDiscardPhase;
import catan.settlers.network.client.commands.game.PlaceElmtsSetupPhaseCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.WaitForPlayerCommand;
import catan.settlers.network.client.commands.game.WaitForSevenDiscardCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.game.handlers.RollDicePhaseHandler;
import catan.settlers.server.model.game.handlers.SetupPhaseHandler;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Knight.KnightType;
import catan.settlers.server.model.units.Port;
import catan.settlers.server.model.units.Village;

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

	private int redDie, yellowDie, eventDie;
	private int barbarianHordeCounter;

	private SetupPhaseHandler setupPhaseHandler;
	private RollDicePhaseHandler rollDicePhaseHandler;

	public Game(int id, Credentials owner) {
		this.id = id;
		this.currentPhase = GamePhase.READYTOJOIN;
		this.participants = new ArrayList<>();

		this.gamePlayersManager = new GamePlayersManager(owner, participants, id);
		this.gameBoardManager = new GameBoardManager();

		this.setupPhaseHandler = new SetupPhaseHandler(this);
		this.rollDicePhaseHandler = new RollDicePhaseHandler(this);
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

		if (currentSetOfOpponentMove != null) {
			handleSetOfOpponentMove(player, data);
			return;
		}

		switch (currentPhase) {
		case SETUPPHASEONE:
			setupPhaseHandler.handle(player, data, true);
			break;
		case SETUPPHASETWO:
			setupPhaseHandler.handle(player, data, false);
			break;
		case ROLLDICEPHASE:
			rollDicePhaseHandler.handle(player, data);
			break;
		case TURNPHASE:
			turnPhase(player, data);
		default:
			break;
		}
	}

	private void handleSetOfOpponentMove(Player player, TurnData data) {
		switch (currentSetOfOpponentMove.getMoveType()) {
		case SEVEN_DISCARD_CARDS:
			sevenDiscardCards(player, data.getSevenResources());
			break;
		}
	}

	/* ======================== Game phases logic ======================== */

	private void turnPhase(Player sender, TurnData data) {
		switch (data.getAction()) {
		case BUILDSETTLEMENT:
			Intersection selected = gameBoardManager.getBoard()
					.getIntersectionById(data.getIntersectionSelection().getId());
			if (selected.canBuild()) {
				/*
				 * if (sender.getResourceAmount(ResourceType.BRICK) > 0 &&
				 * sender.getResourceAmount(ResourceType.GRAIN) > 0 &&
				 * sender.getResourceAmount(ResourceType.WOOL) > 0 &&
				 * sender.getResourceAmount(ResourceType.LUMBER) > 0) {
				 */
				if (selected.isPortable()) {
					Port v = new Port(sender);
					selected.setUnit(v);
				} else {
					Village v = new Village(sender);
					selected.setUnit(v);
				}
				sender.removeResource(ResourceType.BRICK, 1);
				sender.removeResource(ResourceType.GRAIN, 1);
				sender.removeResource(ResourceType.WOOL, 1);
				sender.removeResource(ResourceType.LUMBER, 1);
				// }
			}
			break;
		case BUILDROAD:
			Edge edgeSelect = gameBoardManager.getBoard().getEdgeById(data.getEdgeSelection().getId());
			edgeSelect.setOwner(sender);
			sender.removeResource(ResourceType.BRICK, 1);
			sender.removeResource(ResourceType.LUMBER, 1);
			currentPlayer.sendCommand(new UpdateResourcesCommand(currentPlayer.getResources()));
			sender.sendCommand(new UpdateGameBoardCommand(gameBoardManager.getBoardDeepCopy()));

			break;
		case UPGRADESETTLEMENT:
			IntersectionUnit village = gameBoardManager.getBoard()
					.getIntersectionById(data.getIntersectionSelection().getId()).getUnit();
			if (village instanceof Village) {
				if (sender.getResourceAmount(ResourceType.ORE) >= 3
						&& sender.getResourceAmount(ResourceType.GRAIN) >= 2) {
					((Village) village).upgradeToCity();
					sender.removeResource(ResourceType.ORE, 3);
					sender.removeResource(ResourceType.GRAIN, 2);
				}
			}
			break;
		case BUILDKNIGHT:
			Intersection newKnight = gameBoardManager.getBoard()
					.getIntersectionById(data.getIntersectionSelection().getId());
			if (newKnight.getUnit() == null) {
				if (sender.getResourceAmount(ResourceType.ORE) >= 1 && sender.getResourceAmount(ResourceType.WOOL) >= 1
						&& newKnight.connected(sender)) {
					IntersectionUnit k = new Knight(sender);
					newKnight.setUnit(k);
					sender.removeResource(ResourceType.ORE, 1);
					sender.removeResource(ResourceType.WOOL, 1);
				}
			}
			break;
		case UPGRADEKNIGHT:
			IntersectionUnit knight = gameBoardManager.getBoard()
					.getIntersectionById(data.getIntersectionSelection().getId()).getUnit();
			if (knight instanceof Knight) {
				switch (((Knight) knight).getKnightType()) {
				case BASIC_KNIGHT:
					if (sender.canHire(KnightType.STRONG_KNIGHT)) {
						if (sender.getResourceAmount(ResourceType.WOOL) >= 1
								&& sender.getResourceAmount(ResourceType.GRAIN) >= 1) {
							((Knight) knight).upgradeKnight();
							sender.removeResource(ResourceType.WOOL, 1);
							sender.removeResource(ResourceType.GRAIN, 1);
						}
					}
					break;
				case STRONG_KNIGHT:
					if (sender.canHire(KnightType.MIGHTY_KNIGHT) && sender.hasBarracks()) {
						if (sender.getResourceAmount(ResourceType.WOOL) >= 1
								&& sender.getResourceAmount(ResourceType.GRAIN) >= 1) {
							((Knight) knight).upgradeKnight();
							sender.removeResource(ResourceType.WOOL, 1);
							sender.removeResource(ResourceType.GRAIN, 1);
						}
					}
					break;
				case MIGHTY_KNIGHT:
					// Can't upgrade a mighty knight!
					break;
				}
			}
			break;
		case ENDTURN:
			currentPlayer = nextPlayer();
			break;
		default:
			break;
		}

	}

	private void sevenDiscardCards(Player player, HashMap<ResourceType, Integer> sevenResources) {
		int nbSelectedResources = 0;
		for (ResourceType rtype : ResourceType.values()) {
			nbSelectedResources += sevenResources.get(rtype);
		}

		if (nbSelectedResources == Math.floor(player.getNbResourceCards() / 2)) {
			for (ResourceType rtype : ResourceType.values()) {
				player.removeResource(rtype, sevenResources.get(rtype));
			}
			player.sendCommand(new UpdateResourcesCommand(player.getResources()));
			currentSetOfOpponentMove.playerResponded(player);

			if (!currentSetOfOpponentMove.allPlayersResponded()) {
				for (Player p : participants) {
					if (!currentSetOfOpponentMove.contains(p))
						player.sendCommand(new WaitForSevenDiscardCommand(currentSetOfOpponentMove.nbOfResponses(),
								currentSetOfOpponentMove.nbOfPlayers()));
				}
			} else {
				currentSetOfOpponentMove = null;
				for (Player p : participants)
					p.sendCommand(new EndOfSevenDiscardPhase());
			}
		} else {
			// In case of failure, re-send the discard card command
			player.sendCommand(new DiscardCardsCommand());
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

	public Player nextPlayer() {
		int index = (participants.indexOf(currentPlayer) + 1) % participants.size();
		return participants.get(index);
	}

	public Player previousPlayer() {
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

	public void updateAllPlayers() {
		for (Player p : participants) {
			p.sendCommand(new UpdateGameBoardCommand(gameBoardManager.getBoardDeepCopy()));
		}
	}

	public void sendToAllPlayers(ServerToClientCommand cmd) {
		for (Player p : participants) {
			p.sendCommand(cmd);
		}
	}

	public ArrayList<Player> getParticipants() {
		ArrayList<Player> ret = new ArrayList<>();
		for (Player p : participants)
			ret.add(p);
		return ret;
	}

	public void setGamePhase(GamePhase phase) {
		this.currentPhase = phase;
	}

	public void setDice(int redDie, int yellowDie, int eventDie) {
		this.redDie = redDie;
		this.yellowDie = yellowDie;
		this.eventDie = eventDie;
	}

	public SetOfOpponentMove getCurrentSetOfOpponentMove() {
		return currentSetOfOpponentMove;
	}

	public void setCurSetOfOpponentMove(SetOfOpponentMove set) {
		this.currentSetOfOpponentMove = set;
	}

	public int getBarbarianHordeCounter() {
		return barbarianHordeCounter;
	}

	public void increaseBarbarianHordeCounter() {
		this.barbarianHordeCounter++;
	}

	public void resetBarbarianHordeCounter() {
		this.barbarianHordeCounter = 0;
	}
}
