package catan.settlers.server.model.game.handlers.set;

import java.io.Serializable;
import java.util.HashSet;

import catan.settlers.network.server.Credentials;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.TurnData;

/**
 * This class keeps track of the responses from the players. It is useful for
 * actions that require to wait for multiple players before continuing. FOr
 * instance, when a seven is rolled, it is necessary to wait until all the
 * players that have more than seven cards discard half of their cards. In this
 * case, an instance of this class can be used to know if all he player have
 * responded
 */
public abstract class SetOfOpponentMove implements Serializable {

	private static final long serialVersionUID = 8980170217547407983L;

	private HashSet<Credentials> players;
	private int totalNbOfPlayers;

	public SetOfOpponentMove() {
		this.players = new HashSet<>();
		this.totalNbOfPlayers = 0;
	}

	public abstract void handle(Game game, Player sender, TurnData data);

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

	public boolean contains(Player player) {
		return players.contains(player.getCredentials());
	}

	public boolean isEmpty() {
		return players.isEmpty();
	}

	public HashSet<Credentials> getPlayers() {
		HashSet<Credentials> ret = new HashSet<>();
		ret.addAll(players);
		return ret;
	}

}
