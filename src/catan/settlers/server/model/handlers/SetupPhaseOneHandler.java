package catan.settlers.server.model.handlers;

import java.util.ArrayList;

import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;

public class SetupPhaseOneHandler {

	private ArrayList<Player> participants;

	public SetupPhaseOneHandler(ArrayList<Player> participants, Game game) {
		this.participants = participants;
	}

	public void handle() {
		for(Player p : participants) {
			// Send rollDice command
		}
	}

}
