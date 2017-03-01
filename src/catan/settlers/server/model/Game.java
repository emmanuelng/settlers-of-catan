package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Game extends Thread implements Serializable {

	private static final long serialVersionUID = 1L;
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

	public ArrayList<String> getParticipantsUsernames() {
		ArrayList<String> list = new ArrayList<>();

		for (Player p : participants) {
			list.add(p.getUsername());
		}

		return list;
	}

	@Override
	public void run() {
		// TODO Run the game
		super.run();
	}
}
