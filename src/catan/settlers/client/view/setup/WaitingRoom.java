package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;

public class WaitingRoom implements ActionListener {
	private JPanel waitingRoomPanel;
	private JLabel player1, player2, player3;
	private JButton goButton, back;

	public WaitingRoom(ArrayList<String> participants,int gameId) {
		System.out.println(MainFrame.getInstance().getContentPane());
		waitingRoomPanel = new JPanel();
		JLabel test = new JLabel("test");
		goButton = new JButton("Go");
		back = new JButton("Back");

		for (String s: participants) {
			waitingRoomPanel.add(new JLabel(s));
			System.out.println(s);
		}
		waitingRoomPanel.add(goButton);
		waitingRoomPanel.add(back);
		waitingRoomPanel.add(test);

		goButton.addActionListener(this);
		waitingRoomPanel.setVisible(true);
	}

	public JPanel getPanel() {
		return waitingRoomPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == goButton) {
			System.out.println("successful go");
		} else if (arg0.getSource() == back) {
			ClientModel.instance.sendCommand(new CancelJoinGameCommand)
		}
	}
}
