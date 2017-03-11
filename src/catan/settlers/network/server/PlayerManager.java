package catan.settlers.network.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.common.utils.File;
import catan.settlers.network.client.commands.AuthenticationResponseCommand.Status;
import catan.settlers.server.model.Player;

public class PlayerManager {

	private static final String registeredPlayersFileName = "players.dat";

	private File registeredPlayersFile;
	private ArrayList<Player> registeredPlayers;
	private HashMap<Player, Session> playerSessionMap;

	public PlayerManager() throws IOException {
		registeredPlayersFile = new File(registeredPlayersFileName);
		registeredPlayers = loadRegisteredPlayers();
		playerSessionMap = new HashMap<>();
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

	public Status authenticate(String username, String password, Session sender) {
		for (Player p : registeredPlayers) {
			if (p.getUsername().equals(username) && p.comparePassword(password)) {
				if (isPlayerConnected(p)) {
					return Status.ALREADY_CONNECTED;
				} else {
					playerSessionMap.put(p, sender);
					return Status.SUCCESS;
				}
			}
		}

		return Status.INVALID_CREDENTIALS;
	}

	public Player getPlayerByUsername(String username) {
		for (Player p : registeredPlayers) {
			if (p.getUsername().equals(username)) {
				return p;
			}
		}

		return null;
	}

	public Session getSessionByPlayer(Player player) {
		return playerSessionMap.get(player);
	}

	public Player getPlayerBySession(Session s) {
		for (Player player : playerSessionMap.keySet()) {
			if (playerSessionMap.get(player) == s) {
				return player;
			}
		}
		return null;
	}

	public boolean isPlayerConnected(Player player) {
		return playerSessionMap.get(player) != null;
	}
	
	public void removeSession(Session session) {
		for (Player player : playerSessionMap.keySet()) {
			if (playerSessionMap.get(player) == session) {
				playerSessionMap.remove(player);
				return;
			}
		}
	}

	/**
	 * Save/Load the list of players
	 */

	private void saveRegisteredPlayers() {
		registeredPlayersFile.write(registeredPlayers);
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Player> loadRegisteredPlayers() {
		ArrayList<Player> loadedList = (ArrayList<Player>) registeredPlayersFile.read();

		if (loadedList == null) {
			loadedList = new ArrayList<>();
		}

		return loadedList;
	}
}
