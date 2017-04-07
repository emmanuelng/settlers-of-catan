package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import catan.settlers.network.client.commands.MoreReadyPlayersCommand;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.client.commands.StartGameCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.SessionObserver;
import catan.settlers.server.model.Game.GamePhase;

public class GamePlayersManager implements Serializable, SessionObserver {

	private static final long serialVersionUID = -3532619513897990826L;

	public static enum JoinStatus {
		SUCCESS, INVALID_GAME_STATUS, ROOM_FULL, ALREADY_JOINED, FAILURE
	};

	private ArrayList<Player> participants;
	private HashMap<Credentials, Boolean> readyPlayers;
	private int gameId;

	public GamePlayersManager(Credentials owner, ArrayList<Player> participants, int gameId) {
		this.participants = participants;
		this.readyPlayers = new HashMap<>();
		this.gameId = gameId;

		addPlayer(owner);
	}

	public JoinStatus addPlayer(Credentials playerCred) {
		if (getGame() != null) {
			if (getGame().getGamePhase() != GamePhase.READYTOJOIN) {
				return JoinStatus.INVALID_GAME_STATUS;
			}
		}

		if (!participants.contains(playerCred) && participants.size() <= Game.MAX_NB_OF_PLAYERS) {
			Player new_player = new Player(playerCred);
			participants.add(new_player);
			readyPlayers.put(playerCred, false);
			new_player.getSession().registerObserver(this);
			return JoinStatus.SUCCESS;
		} else {
			if (participants.contains(playerCred))
				return JoinStatus.ALREADY_JOINED;
			if (participants.size() <= Game.MAX_NB_OF_PLAYERS)
				return JoinStatus.ROOM_FULL;
		}

		return JoinStatus.FAILURE;
	}

	public void removePlayer(Credentials cred) {
		participants.remove(getPlayerByCredentials(cred));
		readyPlayers.remove(cred);
	}

	public ArrayList<String> getParticipantsUsernames() {
		ArrayList<String> list = new ArrayList<>();
		for (Player p : participants) {
			list.add(p.getUsername());
		}

		return list;
	}

	public ArrayList<Player> getParticipants() {
		return participants;
	}

	public boolean isParticipant(Credentials credentials) {
		return getPlayerByCredentials(credentials) != null;
	}

	public void playerIsReady(Credentials cred) {
		if (participants.contains(getPlayerByCredentials(cred))) {
			readyPlayers.put(cred, true);
			if (allPlayersReady() && participants.size() == Game.MAX_NB_OF_PLAYERS) {
				Collections.shuffle(participants);
				sendToAll(new StartGameCommand());
				getGame().startGame();
			} else {
				int ready_players = getNbOfReadyPlayers();
				MoreReadyPlayersCommand cmd = new MoreReadyPlayersCommand(ready_players, Game.MAX_NB_OF_PLAYERS,
						getParticipantsUsernames(), gameId);
				sendToAll(cmd);
			}
		}
	}

	public int getNbOfReadyPlayers() {
		int result = 0;
		for (Credentials cred : readyPlayers.keySet()) {
			if (readyPlayers.get(cred)) {
				result++;
			}
		}
		return result;
	}

	@Override
	public void sessionWasClosed(Credentials credentials) {
		// TODO Handle player left the game
	}

	private void sendToAll(ServerToClientCommand cmd) {
		for (Player p : participants) {
			p.sendCommand(cmd);
		}
	}

	private boolean allPlayersReady() {
		for (Credentials cred : readyPlayers.keySet()) {
			if (!readyPlayers.get(cred)) {
				return false;
			}
		}
		return true;
	}

	private Game getGame() {
		return Server.getInstance().getGameManager().getGameById(gameId);
	}

	public Player getPlayerByCredentials(Credentials cred) {
		for (Player player : participants) {
			if (player.getUsername().equals(cred.getUsername())) {
				return player;
			}
		}
		return null;
	}

	public Player getPlayerByUsername(String username) {
		for (Player p : participants)
			if (p.getUsername().equals(username))
				return p;

		return null;
	}

}
