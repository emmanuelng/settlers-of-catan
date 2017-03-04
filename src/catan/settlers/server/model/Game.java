package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Game extends Thread implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Player> participants;
	private int id;

	public Game(int id, Player owner) {
		participants = new ArrayList<>();
		participants.add(owner);
	}

	public int getGameId() {
		return id;
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

	public synchronized ArrayList<String> getParticipantsUsernames() {
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
