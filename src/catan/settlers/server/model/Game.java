package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Game extends Thread implements Serializable {

	public static final int MAX_NB_OF_PLAYERS = 1;

	private static final long serialVersionUID = 1L;
	private int id;

	private ArrayList<Player> participants;
	private GamePlayersManager gamePlayersManager;
	private GameBoardManager gameBoardManager;

	public Game(int id, Player owner) {
		this.id = id;
		this.participants = new ArrayList<>();
		this.gamePlayersManager = new GamePlayersManager(owner, participants, id);
		this.gameBoardManager = new GameBoardManager();

		start();
	}

	@Override
	public void run() {
		while (!gamePlayersManager.canStartGame()) {
			// Wait until all the players are ready
		}
		
		System.out.println("Game started!");
		
	}

	public int getGameId() {
		return id;
	}

	public GamePlayersManager getPlayersManager() {
		return gamePlayersManager;
	}

	public GameBoardManager getGameBoardManager() {
		return gameBoardManager;
	}
}
