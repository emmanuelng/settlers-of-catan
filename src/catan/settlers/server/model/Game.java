package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Game extends Thread implements Serializable {

	private ArrayList<Player> participants;

	public Game(String ownerUsername) {
		// TODO add owner to the list of participants
		participants = new ArrayList<>();
	}

	public boolean addPlayer(Player player) {
		if (!participants.contains(player) && participants.size() < 3) {
			participants.add(player);
			return true;
		}
		
		return false;
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
