package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.network.client.commands.MoreReadyPlayersCommand;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.client.commands.StartGameCommand;

public class GamePlayersManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Player> participants;
	private Player currentPlayer;
	private HashMap<Player, Boolean> readyPlayers;
	private int gameId;

	public GamePlayersManager(Player owner, ArrayList<Player> participants, int gameId) {
		this.participants = participants;
		this.readyPlayers = new HashMap<>();
		this.participants.add(owner);
		this.gameId = gameId;
	}

	public synchronized boolean addPlayer(Player player) {
		if (!participants.contains(player) && participants.size() < Game.MAX_NB_OF_PLAYERS) {
			participants.add(player);
			return true;
		}

		return false;
	}

	public synchronized void removePlayer(Player player) {
		participants.remove(player);
		readyPlayers.remove(player);
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

	public void setCurPlayer(Player p) {
		currentPlayer = p;
	}

	public Player getCurPlayer() {
		return currentPlayer;
	}

	public int getNumPlayers() {
		return participants.size();
	}

	public Player getPlayer(int i) {
		if (i >= 0 && i < participants.size()) {
			return participants.get(i);
		} else {
			return null;
		}
	}

	public void playerIsReady(Player player) {
		if (participants.contains(player)) {
			readyPlayers.put(player, true);
			if (allPlayersReady() && participants.size() == Game.MAX_NB_OF_PLAYERS) {
				sendToAll(new StartGameCommand());
			} else {
				int ready_players = getNbOfReadyPlayers();
				MoreReadyPlayersCommand cmd = new MoreReadyPlayersCommand(ready_players, Game.MAX_NB_OF_PLAYERS,
						getParticipantsUsernames(), gameId);
				sendToAll(cmd);
			}
		}
	}

	private void sendToAll(ServerToClientCommand cmd) {
		for (Player p : participants) {
			p.sendCommand(cmd);
		}

	}

	private boolean allPlayersReady() {
		for (Player p : readyPlayers.keySet()) {
			if (!readyPlayers.get(p)) {
				return false;
			}
		}
		return true;
	}

	public int getNbOfReadyPlayers() {
		int result = 0;
		for (Player p : readyPlayers.keySet()) {
			if (readyPlayers.get(p)) {
				result++;
			}
		}
		return result;
	}
}
