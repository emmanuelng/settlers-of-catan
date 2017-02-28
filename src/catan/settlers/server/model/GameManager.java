package catan.settlers.server.model;

import java.util.ArrayList;
import java.util.Collections;

public class GameManager {
	
	private ArrayList<Game> games;
	
	public GameManager() {
		this.games = new ArrayList<>();
	}
	
	public void createGame(String ownerUsername) {
		games.add(new Game(ownerUsername));
	}
	
	public ArrayList<Game> getListOfGames() {
		return (ArrayList<Game>) Collections.unmodifiableList(games);
	}
}
