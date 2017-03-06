package catan.settlers.client.model;

import catan.settlers.server.model.Game;

public class GameRepresentation {
	
	private int id;
	
	public GameRepresentation(Game game) {
		this.id = game.getGameId();
	}
	
	public int getGameId() {
		return id;
	}
}
