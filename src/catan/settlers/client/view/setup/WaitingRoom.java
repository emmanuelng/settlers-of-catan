package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WaitingRoom implements ActionListener {
	private JPanel waitingRoomPanel;
	private JLabel player1, player2, player3;
	private JButton goButton, back;

	public WaitingRoom(ArrayList<String> participants) {
		waitingRoomPanel = new JPanel();
		goButton = new JButton("Go");
		back = new JButton("Back");

		for (int i = 0; i < 3; i++) {
			if (participants.get(i) != null) {
				String username = participants.get(i);
				waitingRoomPanel.add(new JLabel(username));
			}
		}
		waitingRoomPanel.add(goButton);
		waitingRoomPanel.add(back);

		goButton.addActionListener(this);
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

		}
	}
}
