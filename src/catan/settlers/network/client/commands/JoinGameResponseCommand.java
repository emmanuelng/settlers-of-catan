package catan.settlers.network.client.commands;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import catan.settlers.client.view.setup.MainFrame;
import catan.settlers.client.view.setup.WaitingRoom;
import catan.settlers.server.model.Player;

public class JoinGameResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean success;
	private ArrayList<String> currentListOfPlayers;
	private int gameID;

	public JoinGameResponseCommand(boolean success, ArrayList<String> currentListOfPlayers,int gameID) {
		this.success = success;
		this.currentListOfPlayers = currentListOfPlayers;
		this.gameID=gameID;
	}

	@Override
	public void execute() {
		// TODO Handle Join game result here
		
		System.out.println(success);
		if (success) {
			WaitingRoom room = new WaitingRoom(currentListOfPlayers,gameID);
			
			// Update the frame
			MainFrame.getInstance().switchScreen(room.getPanel());
			
		} else {
			JOptionPane.showMessageDialog(new JLabel(), "Room full");
		}
	}
}
