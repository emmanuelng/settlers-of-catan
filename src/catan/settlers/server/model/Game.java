package catan.settlers.server.model;

import java.io.Serializable;

public class Game extends Thread implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	
	private GamePlayersManager gamePlayersManager;
	private GameBoardManager gameBoardManager;
	private GamePhase phase;
	
	private int redDie;
	private int yellowDie;
	
	public enum GamePhase {
		READYTOJOIN, SETUPPHASEONE, SETUPPHASETWO, TURNPHASEONE, TURNDICEROLL,TURNPHASETWO, COMPLETED
	}

	public Game(int id, Player owner) {
		this.id = id;
		this.gamePlayersManager = new GamePlayersManager(owner);
		this.gameBoardManager = new GameBoardManager();
		this.phase = GamePhase.READYTOJOIN;
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
	
	public GamePhase getPhase() {
		return phase;
	}
	
	public void setPhase(GamePhase p) {
		phase = p;
	}
	
	@Override
	public void run() {
		while (true) {
			switch (phase) {
			case READYTOJOIN: 
				System.out.println("Phase is " + phase.toString());
				break;
			case SETUPPHASEONE:
				System.out.println("Phase is " + phase.toString());
				// Roll to see who goes first
				int highestRoll = 0;
				Player goesFirst;
				for (int i = 0; i < gamePlayersManager.getNumPlayers(); i++) {
					gamePlayersManager.setCurPlayer(gamePlayersManager.getPlayer(i));
					System.out.println("Waiting for roll");
					
					// wait for player to click roll dice
					if (redDie+yellowDie > highestRoll) {
						highestRoll = (redDie + yellowDie);
						goesFirst = gamePlayersManager.getPlayer(i);
					}
				}
				System.out.println("Highest roll of " + highestRoll);
				break;
			case SETUPPHASETWO:
				break;
			}
		}
		
		// TODO Run the game
		//super.run();
	}
	
	public void rollDice() {
		redDie = (int)Math.ceil((Math.random()*6));
		yellowDie = (int)Math.ceil((Math.random()*6));
	}
}
