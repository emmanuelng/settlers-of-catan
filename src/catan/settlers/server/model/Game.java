package catan.settlers.server.model;

import java.util.ArrayList;

public class Game extends Thread {
	
	private ArrayList<Player> participants;
	
	public Game(String ownerUsername) {
		// TODO add owner to the list of participants
		participants = new ArrayList<>();
	}
	
	public void addPlayer(Player player) {
		if (!participants.contains(player)) {
			participants.add(player);
			if (participants.size() == 3) {
				start();
			}
		}
	}
	
	public void removePlayer(Player player) {
		participants.remove(player);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
}
