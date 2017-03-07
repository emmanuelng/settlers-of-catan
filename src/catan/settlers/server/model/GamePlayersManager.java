package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

public class GamePlayersManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Player> participants;

	public GamePlayersManager(Player owner) {
		participants = new ArrayList<>();
		participants.add(owner);
	}

	public synchronized boolean addPlayer(Player player) {
		System.out.println(participants.size());
		if (!participants.contains(player) && participants.size() < 3) {
			participants.add(player);
			return true;
		}

		return false;
	}

	public synchronized void removePlayer(Player player) {
		participants.remove(player);
	}

	public boolean shouldRemoveGame() {
		return participants.size() == 0;
	}

	public synchronized ArrayList<String> getParticipantsUsernames() {
		ArrayList<String> list = new ArrayList<>();

		for (Player p : participants) {
			list.add(p.getUsername());
		}

		return list;
	}

	public boolean isParticipant(Player player) {
		return participants.contains(player);
	}
}
