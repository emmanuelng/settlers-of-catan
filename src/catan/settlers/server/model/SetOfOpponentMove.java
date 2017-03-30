package catan.settlers.server.model;

import java.io.Serializable;
import java.util.HashSet;

import catan.settlers.network.server.Credentials;

/**
 * This class keeps track of the responses from the players. It is useful for
 * actions that require to wait for multiple players before continuing. FOr
 * instance, when a seven is rolled, it is necessary to wait until all the
 * players that have more than seven cards discard half of their cards. In this
 * case, an instance of this class can be used to know if all he player have
 * responded
 */
public class SetOfOpponentMove implements Serializable {

	public static enum MoveType {
		SEVEN_DISCARD_CARDS
	}

	private static final long serialVersionUID = 1L;
	private HashSet<Credentials> players;
	private MoveType type;
	private int totalNbOfPlayers;

	public SetOfOpponentMove(MoveType type) {
		this.players = new HashSet<>();
		this.type = type;
		this.totalNbOfPlayers = 0;
	}

	public void waitForPlayer(Credentials c) {
		players.add(c);
		totalNbOfPlayers++;
	}

	public void playerResponded(Credentials cred) {
		players.remove(cred);
	}

	public void playerResponded(Player p) {
		playerResponded(p.getCredentials());
	}

	public void waitForPlayer(Player p) {
		waitForPlayer(p.getCredentials());
	}

	public boolean allPlayersResponded() {
		return players.isEmpty();
	}

	public int nbOfResponses() {
		return totalNbOfPlayers - players.size();
	}

	public int nbOfPlayers() {
		return totalNbOfPlayers;
	}

	public MoveType getMoveType() {
		return type;
	}

	public boolean contains(Player player) {
		return players.contains(player.getCredentials());
	}

	public boolean isEmpty() {
		return players.isEmpty();
	}

}
