package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Game extends Thread implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	
	private GamePlayersManager gamePlayersManager;
	private GameBoardManager gameBoardManager;

	public Game(int id, Player owner) {
		this.id = id;
		this.gamePlayersManager = new GamePlayersManager(owner);
		this.gameBoardManager = new GameBoardManager();
	}

	public int getGameId() {
		return id;
	}
	
	public GamePlayersManager getPlayersManager() {
		return gamePlayersManager;
	}
	
	@Override
	public void run() {
		// TODO Run the game
		super.run();
	}
}
