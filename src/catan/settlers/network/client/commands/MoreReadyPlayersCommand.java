package catan.settlers.network.client.commands;

import java.util.ArrayList;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.setup.WaitingRoom;

/**
 * Is sent when a player indicates that he is ready, but not enough players are
 * ready to start the game
 */
public class MoreReadyPlayersCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private int nbReadyPlayers;
	private int maxNbPlayers;
	private ArrayList<String> participants;
	private int gameId;

	public MoreReadyPlayersCommand(int nbReadyPlayers, int maxNbPlayers, ArrayList<String> participants, int gameId) {
		this.nbReadyPlayers = nbReadyPlayers;
		this.maxNbPlayers = maxNbPlayers;
		this.participants = participants;
		this.gameId = gameId;
	}

	@Override
	public void execute() {
		WaitingRoom room = new WaitingRoom(participants, gameId, nbReadyPlayers, maxNbPlayers);
		ClientWindow.getInstance().getSetupWindow().setScreen(room);
	}

}
