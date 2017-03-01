package catan.settlers.network.client.commands;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import catan.settlers.client.view.setup.MainFrame;
import catan.settlers.server.model.Player;

public class JoinGameResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean success;
	private ArrayList<Player> currentListOfPlayers;

	public JoinGameResponseCommand(boolean success, ArrayList<Player> currentListOfPlayers) {
		this.success = success;
		this.currentListOfPlayers = currentListOfPlayers;
	}

	@Override
	public void execute() {
		// TODO Handle Join game result here
		if (success) {
			WaitingRoom room = new WaitingRoom(currentListOfPlayers);
			
			// Update the frame
			MainFrame.getInstance().remove(MainFrame.getInstance().getContentPane());
			MainFrame.getInstance().add(room.getPanel());
			MainFrame.getInstance().setContentPane(room.getPanel());
			MainFrame.getInstance().revalidate();
			MainFrame.getInstance().repaint();
		} else {
			JOptionPane.showMessageDialog(new JLabel(), "Room full");
		}
	}
}
