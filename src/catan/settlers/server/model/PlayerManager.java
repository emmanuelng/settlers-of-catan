package catan.settlers.server.model;

import java.io.IOException;
import java.util.ArrayList;

import catan.settlers.common.utils.File;
import catan.settlers.network.server.Session;

public class PlayerManager {

	private static final String registeredPlayersFileName = "players.dat";

	private File registeredPlayersFile;
	private ArrayList<Player> registeredPlayers;

	public PlayerManager() throws IOException {
		registeredPlayersFile = new File(registeredPlayersFileName);
		registeredPlayers = loadRegisteredPlayers();
	}

	public boolean register(String username, String password) {
		for (Player p : registeredPlayers) {
			if (p.getUsername().equals(username)) {
				return false;
			}
		}

		registeredPlayers.add(new Player(username, password));
		saveRegisteredPlayers();
		return true;
	}

	public boolean authenticate(String username, String password, Session sender) {
		for (Player p : registeredPlayers) {
			if (p.getUsername().equals(username) && p.comparePassword(password)) {
				p.setCurrentSession(sender);
				sender.setPlayer(p);
				return true;
			}
		}
		
		return false;
	}

	private void saveRegisteredPlayers() {
		registeredPlayersFile.write(registeredPlayers);
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Player> loadRegisteredPlayers() {
		ArrayList<Player> loadedList = (ArrayList<Player>) registeredPlayersFile.read();

		if (loadedList == null) {
			System.out.println("No player file found");
			loadedList = new ArrayList<>();
		} else {
			System.out.println("Loaded list of players");
		}

		return loadedList;
	}

	public Player getPlayerByUsername(String username) {
		for (Player p : registeredPlayers) {
			if (p.getUsername().equals(username)) {
				return p;
			}
		}
		
		return null;
	}
}
