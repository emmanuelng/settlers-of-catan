package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.network.server.Server;

public class Game implements Serializable {

	public static enum GamePhase {
		READYTOJOIN, SETUPPHASEONE
	}

	public static final int MAX_NB_OF_PLAYERS = 1;
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
		// Send Place settlement command to current player, wait command to the others
	}

	public void receiveResponse(Player sender, TurnData data) {
		switch(currentPhase) {
		case SETUPPHASEONE:
			setupPhaseOne(sender, data);
			break;
		default:
			break;
		}
		
	}
	
	/* ============ Game phases logic ============ */
	
	private void setupPhaseOne(Player sender, TurnData data) {
		if (sender == currentPlayer) {
			
		}
		
	}
	
	/*============ End of game phases ============*/

	public int getGameId() {
		return id;
	}

	public void nextPlayer() {
		int index = (participants.indexOf(currentPlayer) + 1) % participants.size();
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
}
