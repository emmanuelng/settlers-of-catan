package catan.settlers.network.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.common.utils.File;
import catan.settlers.network.client.commands.AuthenticationResponseCommand.Status;

public class AuthenticationManager {

	private static final String registeredPlayersFileName = "players.dat";

	private File registeredPlayersFile;
	private HashMap<Credentials, Session> registeredPlayers;

	public AuthenticationManager() throws IOException {
		registeredPlayersFile = new File(registeredPlayersFileName);
		registeredPlayers = loadRegisteredPlayers();
	}

	public boolean register(String username, String password) {
		for (Credentials cred : registeredPlayers.keySet()) {
			if (cred.getUsername().equals(username)) {
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
				Session curSession = registeredPlayers.get(cred);
				if (curSession != null) {
					// A player is logged in if he/she has an active session
					return Status.ALREADY_CONNECTED;
				} else {
					registeredPlayers.put(cred, sender);
					return Status.SUCCESS;
				}
			}
		}
		return Status.INVALID_CREDENTIALS;
	}

	public Session getSessionByCredentials(Credentials cred) {
		return registeredPlayers.get(cred);
	}

	public Credentials getCredentialsBySession(Session s) {
		for (Credentials cred : registeredPlayers.keySet()) {
			if (registeredPlayers.get(cred) == s) {
				return cred;
			}
		}
		return null;
	}

	public boolean isConnected(Credentials cred) {
		return registeredPlayers.get(cred) != null;
	}

	public void removeSession(Session session) {
		for (Credentials cred : registeredPlayers.keySet()) {
			if (registeredPlayers.get(cred) == session) {
				registeredPlayers.put(cred, null);
				return;
			}
		}
	}

	public Session getSessionByUsername(String username) {
		for (Credentials cred : registeredPlayers.keySet()) {
			if (cred.getUsername().equals(username)) {
				return registeredPlayers.get(cred);
			}
		}
		return null;
	}

	/**
	 * Save/Load the list of players
	 */

	private void saveRegisteredPlayers() {
		ArrayList<Credentials> credentials = new ArrayList<>();
		for (Credentials cred : registeredPlayers.keySet()) {
			credentials.add(cred);
		}
		registeredPlayersFile.write(credentials);
	}

	@SuppressWarnings("unchecked")
	private HashMap<Credentials, Session> loadRegisteredPlayers() {
		Object readObj = registeredPlayersFile.read();
		HashMap<Credentials, Session> result = new HashMap<>();

		ArrayList<Credentials> loadedList = (ArrayList<Credentials>) readObj;

		if (loadedList != null) {
			// Initialize all the players with empty session
			for (Credentials cred : loadedList) {
				result.put(cred, null);
			}
		}

		return result;
	}
}
