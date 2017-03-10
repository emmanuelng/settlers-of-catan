package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.commands.CancelJoinGameCommand;
import catan.settlers.network.server.commands.PlayerReadyCommand;

public class WaitingRoom extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton ReadyButton, backButton;
	private int gameId;

	public WaitingRoom(ArrayList<String> participants, int gameId, int nbReadyPlayers, int maxNbPlayers) {
		this.gameId = gameId;

		JLabel curGameIdLabel = new JLabel("" + gameId);
		JLabel readyPlayersLabel = new JLabel(nbReadyPlayers + "/" + maxNbPlayers);
		ReadyButton = new JButton("Ready");
		backButton = new JButton("Back");

		for (String s : participants) {
			add(new JLabel(s));
		}

		add(readyPlayersLabel);
		add(ReadyButton);
		add(backButton);
		add(curGameIdLabel);

		ReadyButton.addActionListener(this);
		backButton.addActionListener(this);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == ReadyButton) {
			ClientModel.instance.getNetworkManager().sendCommand(new PlayerReadyCommand(gameId));
			ReadyButton.setEnabled(false);
		} else if (arg0.getSource() == backButton) {
			ClientModel.instance.getNetworkManager().sendCommand(new CancelJoinGameCommand(gameId));
		}
	}
}
