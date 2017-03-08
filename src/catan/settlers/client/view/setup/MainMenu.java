package catan.settlers.client.view.setup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.network.server.commands.GetListOfGamesCommand;
import catan.settlers.server.model.Game;

public class MainMenu extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton tutorialButton;
	private JButton playButton;
	private JButton exitButton;
	private JLabel welcomeLabel;

	public MainMenu() {
		welcomeLabel = new JLabel("Welcome " + ClientModel.instance.getUsername());

		tutorialButton = new JButton("Tutorial");
		playButton = new JButton("Play");
		exitButton = new JButton("Exit");

		add(welcomeLabel);
		add(tutorialButton);
		add(playButton);
		add(exitButton);

		playButton.addActionListener(this);
		exitButton.addActionListener(this);
		tutorialButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == playButton) {
			Lobby lobby = new Lobby(new ArrayList<Game>());
			ClientWindow.getInstance().getSetupWindow().setScreen(lobby);
			ClientModel.instance.getNetworkManager().sendCommand(new GetListOfGamesCommand());
		} else if (arg0.getSource() == exitButton) {
			// could add more prompts and stuff, now it's just abrupt closing
			System.exit(0);
		} else if (arg0.getSource() == tutorialButton){
		   try {         
		     java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://www.catan.com/service/prof-easy/prof-easys-interactive-tutorial-settlers-catan"));
		   }
		   catch (java.io.IOException e) {
		       System.out.println(e.getMessage());
		   }
		}
	}
}
