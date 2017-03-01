package catan.settlers.server.model;

import java.util.ArrayList;

public class GameManager {

	private ArrayList<Game> games;
	private int lastId;

	public GameManager() {
		lastId = 0;
		this.games = new ArrayList<>();
	}

	public synchronized void createGame(String ownerUsername) {
		games.add(new Game(lastId, ownerUsername));
		lastId++;
	}

	public synchronized ArrayList<Game> getListOfGames() {
		ArrayList<Game> list = new ArrayList<>();

		for (Game g : games) {
			list.add(g);
		}

		return list;
	}

	public synchronized Game getGameById(int id) {
		for (Game g : games) {
			if (g.getGameId() == id) {
				return g;
			}
		}

		return null;
	}
}
