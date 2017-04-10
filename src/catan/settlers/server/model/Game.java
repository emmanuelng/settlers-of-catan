package catan.settlers.server.model;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.DeclareVictorCommand;
import catan.settlers.network.client.commands.game.GamePhaseChangedCommand;
import catan.settlers.network.client.commands.game.OwnedPortsChangedCommand;
import catan.settlers.network.client.commands.game.PlaceElmtsSetupPhaseCommand;
import catan.settlers.network.client.commands.game.SetParticipantsCommand;
import catan.settlers.network.client.commands.game.UpdateCardsCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.WaitForPlayerCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.server.model.game.handlers.ProgressCardHandler;
import catan.settlers.server.model.game.handlers.RollDicePhaseHandler;
import catan.settlers.server.model.game.handlers.SetupPhaseHandler;
import catan.settlers.server.model.game.handlers.TurnPhaseHandler;
import catan.settlers.server.model.game.handlers.set.SetOfOpponentMove;
import catan.settlers.server.model.map.Hexagon;

public class Game implements Serializable {

	public static enum GamePhase {
		READYTOJOIN, SETUPPHASEONE, SETUPPHASETWO, ROLLDICEPHASE, TURNPHASE, PAUSED
	}

	private static final long serialVersionUID = -5752967531725278325L;
	public static final int MAX_NB_OF_PLAYERS = 1;

	private int id;
	private ArrayList<Player> participants;
	private GamePlayersManager gamePlayersManager;
	private GameBoardManager gameBoardManager;
	private Player currentPlayer;
	private GamePhase currentPhase;
	private SetOfOpponentMove currentSetOfOpponentMove;

	private int redDie, yellowDie, eventDie;
	private int barbarianHordeCounter;
	private Hexagon inventorFirstHex, inventorSecondHex;
	private ProgressCards progressCards;
	private Player largestArmy, longestRoad;
	private boolean bootDrawn;

	private SetupPhaseHandler setupPhaseHandler;
	private RollDicePhaseHandler rollDicePhaseHandler;
	private TurnPhaseHandler turnPhaseHandler;
	private ProgressCardHandler progressCardHandler;

	public Game(int id, Credentials owner) {
		this.id = id;
		this.currentPhase = GamePhase.READYTOJOIN;
		this.participants = new ArrayList<>();

		this.gamePlayersManager = new GamePlayersManager(owner, participants, id);
		this.gameBoardManager = new GameBoardManager();

		this.progressCards = new ProgressCards();
		this.bootDrawn = false;

		this.setupPhaseHandler = new SetupPhaseHandler(this);
		this.rollDicePhaseHandler = new RollDicePhaseHandler(this);
		this.turnPhaseHandler = new TurnPhaseHandler(this);
		this.progressCardHandler = new ProgressCardHandler(this);
	}

	public void startGame() {

		if (currentPhase != GamePhase.PAUSED) {
			this.currentPhase = GamePhase.SETUPPHASEONE;
			this.currentPlayer = participants.get(0);
		}

		for (Player p : participants) {
			sendPlayerState(p);
			if (currentPhase == GamePhase.PAUSED) {
				p.sendCommand(new CurrentPlayerChangedCommand(currentPlayer.getUsername()));
			} else {
				if (p == currentPlayer) {
					p.sendCommand(new PlaceElmtsSetupPhaseCommand(true));
				} else {
					p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
				}
			}
		}

	}

	private void sendPlayerState(Player p) {
		p.sendCommand(new SetParticipantsCommand(gamePlayersManager.getParticipantsUsernames()));
		p.sendCommand(new CurrentPlayerChangedCommand(currentPlayer.getUsername()));
		p.sendCommand(new UpdateResourcesCommand(p.getResources()));
		p.sendCommand(new OwnedPortsChangedCommand(p.getOwnedPorts()));
		p.sendCommand(new GamePhaseChangedCommand(currentPhase));
		p.sendCommand(new UpdateCardsCommand(p.getProgressCards()));
	}

	public void receiveResponse(Credentials credentials, TurnData data) {
		Player player = gamePlayersManager.getPlayerByCredentials(credentials);
		if (player == null)
			return;

		if (currentSetOfOpponentMove != null) {
			currentSetOfOpponentMove.handle(this, player, data);
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
			turnPhaseHandler.handle(player, data);
			break;
		default:
			break;
		}
	}

	public int getGameId() {
		return id;
	}

	public synchronized void saveToFile() {
		try {
			GamePhase prevPhase = currentPhase;
			currentPhase = GamePhase.PAUSED;

			String filename = "saves/game" + getGameId() + ".catan";
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();

			currentPhase = prevPhase;
			System.out.println("Game saved as " + filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		sendToAllPlayers(new GamePhaseChangedCommand(currentPhase));
	}

	public void setDice(int redDie, int yellowDie, int eventDie) {
		this.redDie = redDie;
		this.yellowDie = yellowDie;
		this.eventDie = eventDie;
	}

	public int getRedDie() {
		return redDie;
	}

	public int getYellowDie() {
		return yellowDie;
	}

	public int getEventDie() {
		return eventDie;
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

	public void setInventorFirstHex(Hexagon firstHex) {
		inventorFirstHex = firstHex;
	}

	public void setInventorSecondValue(Hexagon secondValue) {
		inventorSecondHex = secondValue;
	}

	public void inventorCard() {
		int num1 = inventorFirstHex.getNumber();
		int num2 = inventorSecondHex.getNumber();
		inventorFirstHex.setNumber(num2);
		inventorSecondHex.setNumber(num1);
	}

	public ProgressCards getProgressCards() {
		return progressCards;
	}

	public ProgressCardHandler getProgressCardHandler() {
		return progressCardHandler;
	}

	public void setLargestArmy(Player p) {
		largestArmy = p;
	}

	public void setLongestroad(Player p) {
		longestRoad = p;
	}

	public Player getLargestArmy() {
		return largestArmy;
	}

	public Player getLongestroad() {
		return longestRoad;
	}

	public void declareVictor(Player currentPlayer) {
		sendToAllPlayers(new DeclareVictorCommand(currentPlayer.getUsername()));
	}

}
