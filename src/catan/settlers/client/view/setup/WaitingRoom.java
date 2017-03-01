package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.commands.CancelJoinGameCommand;

public class WaitingRoom implements ActionListener {
	private JPanel waitingRoomPanel;
	private JButton goButton, back;
	private int gameId;

	public WaitingRoom(ArrayList<String> participants,int gameId) {
		this.gameId = gameId;
		
		waitingRoomPanel = new JPanel();
		JLabel currentGameID = new JLabel(""+ gameId);
		goButton = new JButton("Go");
		back = new JButton("Back");

		for (String s: participants) {
			waitingRoomPanel.add(new JLabel(s));
		}
		waitingRoomPanel.add(goButton);
		waitingRoomPanel.add(back);
		waitingRoomPanel.add(currentGameID);

		goButton.addActionListener(this);
		back.addActionListener(this);
		waitingRoomPanel.setVisible(true);
	}

	public JPanel getPanel() {
		return waitingRoomPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == goButton) {
			System.out.println("successful go");
		} else if (arg0.getSource() == back) {
			System.out.println("trigger");
			ClientModel.instance.sendCommand(new CancelJoinGameCommand(gameId));
		}
	}
}
