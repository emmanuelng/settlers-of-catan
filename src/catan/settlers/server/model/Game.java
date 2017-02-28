package catan.settlers.server.model;

import java.util.ArrayList;

public class Game {
	
	private ArrayList<Player> participants;
	
	public Game() {
		participants = new ArrayList<>();
	}
	
	public void addPlayer(Player player) {
		if (!participants.contains(player)) {
			participants.add(player);
		}
	}
	
	public void removePlayer(Player player) {
		participants.remove(player);
	}
}
