package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread implements Serializable {

	public static final int MAX_NB_OF_PLAYERS = 1;
	private static final long serialVersionUID = 1L;

	private int id;
	private ArrayList<Player> participants;
	private GamePlayersManager gamePlayersManager;
	private GameBoardManager gameBoardManager;

	private GameThread gameThread;
	private Lock lock;
	private Condition continueGame;

	public Game(int id, Player owner) {
		this.id = id;
		this.participants = new ArrayList<>();
		this.lock = new ReentrantLock();
		this.continueGame = lock.newCondition();

		this.gamePlayersManager = new GamePlayersManager(owner, participants, id, lock, continueGame);
		this.gameBoardManager = new GameBoardManager();
		this.gameThread = new GameThread(participants, lock, continueGame);
	}

	@Override
	public void run() {
		gameThread.start();
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
