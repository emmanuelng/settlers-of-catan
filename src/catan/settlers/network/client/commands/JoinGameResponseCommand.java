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

	public JoinGameResponseCommand(boolean success, ArrayList<String> currentListOfPlayers, int gameID) {
		this.success = success;
		this.currentListOfPlayers = currentListOfPlayers;
		this.gameID = gameID;
	}

	@Override
	public void execute() {
		if (success) {
			WaitingRoom room = new WaitingRoom(currentListOfPlayers, gameID, (currentListOfPlayers.size()==3));
			ClientWindow.getInstance().getSetupWindow().setScreen(room);
		} else {
			JOptionPane.showMessageDialog(new JLabel(), "Room full");
		}
	}
}
