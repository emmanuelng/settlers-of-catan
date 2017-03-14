package catan.settlers.network.client.commands;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.setup.WaitingRoom;
import catan.settlers.server.model.Game;

public class JoinGameResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean success;
	private ArrayList<String> currentListOfPlayers;
	private int gameID;
	private int readyPlayers;
	private int maxPlayers;

	public JoinGameResponseCommand(boolean success, Game game) {
		if (success) {
			this.success = success;
			this.currentListOfPlayers = game.getPlayersManager().getParticipantsUsernames();
			this.gameID = game.getGameId();
			this.readyPlayers = game.getPlayersManager().getNbOfReadyPlayers();
			this.maxPlayers = Game.MAX_NB_OF_PLAYERS;
		} else {
			success = false;
		}
	}

	@Override
	public void execute() {
		if (success) {
			WaitingRoom room = new WaitingRoom(currentListOfPlayers, gameID, readyPlayers, maxPlayers);
			ClientWindow.getInstance().getSetupWindow().setScreen(room);
		} else {
			JOptionPane.showMessageDialog(new JLabel(), "Room full");
		}
	}
}
