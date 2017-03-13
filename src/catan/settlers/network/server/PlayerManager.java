package catan.settlers.network.server;

import java.io.IOException;
import java.util.HashMap;

import catan.settlers.common.utils.File;
import catan.settlers.network.client.commands.AuthenticationResponseCommand.Status;
import catan.settlers.server.model.Player;

public class PlayerManager {

	private static final String registeredPlayersFileName = "players.dat";

	private File registeredPlayersFile;
	private HashMap<Credentials, Player> registeredPlayers;
	private HashMap<Session, Player> sessionPlayerMap;

	public PlayerManager() throws IOException {
		registeredPlayersFile = new File(registeredPlayersFileName);
		sessionPlayerMap = new HashMap<>();
		registeredPlayers = loadRegisteredPlayers();
	}

	public boolean register(String username, String password) {
		for (Credentials p : registeredPlayers.keySet()) {
			if (p.getUsername().equals(username)) {
				return false;
			}
		}

		Credentials new_credentials = new Credentials(username, password);
		registeredPlayers.put(new_credentials, null);
		saveRegisteredPlayers();
		return true;
	}

	public Status authenticate(String username, String password, Session sender) {
		for (Credentials cred : registeredPlayers.keySet()) {
			if (cred.getUsername().equals(username) && cred.comparePassword(password)) {
				Player curPlayer = registeredPlayers.get(cred);
				if (curPlayer != null) {
					return Status.ALREADY_CONNECTED;
				} else {
					Player player = new Player(cred);
					registeredPlayers.put(cred, player);
					sessionPlayerMap.put(sender, player);
					return Status.SUCCESS;
				}
			}
		}
		return Status.INVALID_CREDENTIALS;
	}

	public Player getPlayerByUsername(String username) {
		for (Credentials cred : registeredPlayers.keySet()) {
			if (cred.getUsername().equals(username)) {
				return registeredPlayers.get(cred);
			}
		}

		return null;
	}

	public Session getSessionByPlayer(Player player) {
		for (Session session : sessionPlayerMap.keySet()) {
			if (sessionPlayerMap.get(session) == player) {
				return session;
			}
		}
		return null;
	}

	public Player getPlayerBySession(Session s) {
		return sessionPlayerMap.get(s);
	}

	public boolean isPlayerConnected(Player player) {
		return registeredPlayers.get(player) != null;
	}

	public void removeSession(Session session) {
		Player player = sessionPlayerMap.get(session);
		sessionPlayerMap.remove(session);

		for (Credentials cred : registeredPlayers.keySet()) {
			if (registeredPlayers.get(cred) == player) {
				registeredPlayers.put(cred, null);
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
	private HashMap<Credentials, Player> loadRegisteredPlayers() {
		HashMap<Credentials, Player> loadedList = (HashMap<Credentials, Player>) registeredPlayersFile.read();

		if (loadedList == null) {
			loadedList = new HashMap<>();
		}

		for (Credentials cred : loadedList.keySet()) {
			loadedList.put(cred, null);
		}

		return loadedList;
	}
}
