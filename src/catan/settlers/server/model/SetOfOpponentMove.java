package catan.settlers.server.model;

import java.io.Serializable;
import java.util.HashMap;

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
		DISCARD_CARDS
	}

	private static final long serialVersionUID = 1L;
	private HashMap<Credentials, Boolean> responses;
	private MoveType type;

	public SetOfOpponentMove(MoveType type) {
		this.responses = new HashMap<>();
		this.type = type;
	}

	public void waitForPlayer(Credentials c) {
		responses.put(c, false);
	}

	public void waitForPlayer(Player p) {
		waitForPlayer(p.getCredentials());
	}

	public boolean allPlayersResponded() {
		for (Credentials c : responses.keySet()) {
			if (responses.get(c) == false)
				return false;
		}
		return true;
	}

	public int nbOfResponses() {
		int count = 0;
		for (Credentials c : responses.keySet()) {
			if (responses.get(c))
				count++;
		}
		return count;
	}

	public int nbOfPlayers() {
		return responses.keySet().size();
	}

	public MoveType getMoveType() {
		return type;
	}

	public boolean contains(Player player) {
		return responses.keySet().contains(player.getCredentials());
	}

	public boolean isEmpty() {
		return responses.keySet().isEmpty();
	}

}
