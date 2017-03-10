package catan.settlers.network.client.commands;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.setup.WaitingRoom;

public class JoinGameResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean success;
	private ArrayList<String> currentListOfPlayers;
	private int gameID;
	private int readyPlayers;
	private int maxPlayers;

	public JoinGameResponseCommand(boolean success, ArrayList<String> participants, int gameID, int readyPlayers,
			int maxPlayers) {
		this.success = success;
		this.currentListOfPlayers = participants;
		this.gameID = gameID;
		this.readyPlayers = readyPlayers;
		this.maxPlayers = maxPlayers;
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
