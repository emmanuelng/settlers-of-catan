package catan.settlers.client.model;

import java.io.Serializable;

import catan.settlers.server.model.Game;

/**
 * A light representation of a game. Only contains the required information
 * needed for the game lobby
 */
public class GameRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;
	private int gameId;

	public GameRepresentation(Game game) {
		this.gameId = game.getGameId();
	}

	public int getGameId() {
		return gameId;
	}

}
