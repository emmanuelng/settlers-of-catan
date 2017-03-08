package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.network.server.commands.CancelJoinGameCommand;
import catan.settlers.network.server.commands.StartGameCommand;

public class WaitingRoom extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton goButton, backButton;
	private int gameId;
	

	public WaitingRoom(ArrayList<String> participants, int gameId, boolean canGo) {
		this.gameId = gameId;

		JLabel curGameIdLabel = new JLabel("" + gameId);
		goButton = new JButton("Go");
		backButton = new JButton("Back");

		for (String s : participants) {
			add(new JLabel(s));
		}

		add(goButton);
		add(backButton);
		add(curGameIdLabel);

		goButton.addActionListener(this);
		backButton.addActionListener(this);
		
		goButton.setEnabled(canGo);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == goButton) {
			ClientModel.instance.setCurGameId(gameId);
        	ClientWindow.getInstance().switchToGame();
        	ClientModel.instance.getNetworkManager().sendCommand(new StartGameCommand(gameId));
		} else if (arg0.getSource() == backButton) {
			ClientModel.instance.getNetworkManager().sendCommand(new CancelJoinGameCommand(gameId));
		}
	}
}
