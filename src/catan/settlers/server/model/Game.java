package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.client.commands.game.CurrentPlayerChangedCommand;
import catan.settlers.network.client.commands.game.GamePhaseChangedCommand;
import catan.settlers.network.client.commands.game.OwnedPortsChangedCommand;
import catan.settlers.network.client.commands.game.PlaceElmtsSetupPhaseCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.WaitForPlayerCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.server.model.game.handlers.RollDicePhaseHandler;
import catan.settlers.server.model.game.handlers.SetupPhaseHandler;
import catan.settlers.server.model.game.handlers.SevenDiscardHandler;
import catan.settlers.server.model.game.handlers.TurnPhaseHandler;

public class Game implements Serializable {

	public enum turnAction {
		BUILDSETTLEMENT, BUILDKNIGHT, BUILDROAD, UPGRADESETTLEMENT, UPGRADEKNIGHT, BUILDWALL ,ENDTURN, ACTIVATEKNIGHT, PROGRESSCARD, DISPLACEKNIGHT
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
	private TurnPhaseHandler turnPhaseHandler;
	private SevenDiscardHandler sevenDiscardHandler;

	public Game(int id, Credentials owner) {
		this.id = id;
		this.currentPhase = GamePhase.READYTOJOIN;
		this.participants = new ArrayList<>();

		this.gamePlayersManager = new GamePlayersManager(owner, participants, id);
		this.gameBoardManager = new GameBoardManager();

		this.setupPhaseHandler = new SetupPhaseHandler(this);
		this.rollDicePhaseHandler = new RollDicePhaseHandler(this);
		this.turnPhaseHandler = new TurnPhaseHandler(this);
		this.sevenDiscardHandler = new SevenDiscardHandler(this);
	}

	public void startGame() {
		this.currentPhase = GamePhase.SETUPPHASEONE;
		this.currentPlayer = participants.get(0);

		for (Player p : participants) {

			p.sendCommand(new CurrentPlayerChangedCommand(currentPlayer.getUsername()));
			p.sendCommand(new UpdateResourcesCommand(p.getResources()));
			p.sendCommand(new OwnedPortsChangedCommand(p.getOwnedPorts()));
			sendToAllPlayers(new GamePhaseChangedCommand(currentPhase));

			if (p == currentPlayer) {
				p.sendCommand(new PlaceElmtsSetupPhaseCommand(true));
			} else {
				p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
			}
		}

	}

	public void receiveResponse(Credentials credentials, TurnData data) {
		Player player = gamePlayersManager.getPlayerByCredentials(credentials);
		if (player == null)
			return;

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
			turnPhaseHandler.handle(player, data);
			break;
		default:
			break;
		}
	}

	private void handleSetOfOpponentMove(Player player, TurnData data) {
		switch (currentSetOfOpponentMove.getMoveType()) {
		case SEVEN_DISCARD_CARDS:
			sevenDiscardHandler.handle(player, data.getSevenResources());
			break;
		}
	}

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
}
