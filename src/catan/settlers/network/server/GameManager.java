package catan.settlers.network.server;

import java.util.ArrayList;

import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;

public class GameManager {

	private ArrayList<Game> games;
	private int lastId;

	public GameManager() {
		lastId = 0;
		this.games = new ArrayList<>();
	}

	public Game createGame(Player owner) {
		Game game = new Game(lastId, owner);
		games.add(game);
		lastId++;
		return game;
	}

	public ArrayList<Game> getListOfGames() {
		ArrayList<Game> list = new ArrayList<>();

		for (Game g : games) {
			list.add(g);
		}

		return list;
	}

	public Game getGameById(int id) {
		for (Game g : games) {
			if (g.getGameId() == id) {
				return g;
			}
		}

		return null;
	}

	public void removePlayerFromGames(Player player) {
		for (Game game : games) {
			game.getPlayersManager().removePlayer(player);
		}
	}

	public void removeGame(Game game) {
		games.remove(game);
	}
}
