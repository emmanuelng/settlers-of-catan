package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.commands.GetListOfGamesCommand;
import catan.settlers.server.model.Game;

public class MainMenu implements ActionListener {

	private JButton Tutorial;
	private JButton Play;
	private JButton Exit;
	private JPanel mainPanel;
	private JLabel label1;

	public MainMenu(String username) {
		mainPanel = new JPanel();
		label1 = new JLabel("Welcome " + username);

		Tutorial = new JButton("Tutorial");
		Play = new JButton("Play");
		Exit = new JButton("Exit");

		mainPanel.add(label1);
		mainPanel.add(Tutorial);
		mainPanel.add(Play);
		mainPanel.add(Exit);

		Play.addActionListener(this);
		Exit.addActionListener(this);
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == Play) {
			Lobby lobby = new Lobby(new ArrayList<Game>());
			MainFrame.getInstance().switchScreen(lobby.getPanel());
			ClientModel.instance.sendCommand(new GetListOfGamesCommand());
		} else if (arg0.getSource() == Exit) {
			// could add more prompts and stuff, now it's just abrupt closing
			System.exit(0);
		}
		MainFrame.getInstance().revalidate();
		MainFrame.getInstance().repaint();
	}
}
