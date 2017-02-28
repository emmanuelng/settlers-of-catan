package catan.settlers.client.view.setup;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class MainMenu implements ActionListener {

	private JButton Tutorial;
	private JButton Play;
	private JButton Exit;
	private JPanel mainPanel;
	private JLabel label1, label2, label3;
	private String username;

	public MainMenu(String username) {
		mainPanel = new JPanel();
		this.username = username;
		label1 = new JLabel("Welcome " + username);

		Tutorial = new JButton("Tutorial");
		Play = new JButton("Play");
		Exit = new JButton("Exit");

		mainPanel.add(label1);
		mainPanel.add(Tutorial);
		mainPanel.add(Play);
		mainPanel.add(Exit);

		// Tutorial.addActionListener()
		Play.addActionListener(this);
		Exit.addActionListener(this);
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public void actionPerformed(ActionEvent arg0) {
		JFrame topFrame = MainFrame.getInstance();

		if (arg0.getSource() == Play) {
			Lobby lobby = new Lobby(username);
			topFrame.remove(mainPanel);
			topFrame.add(lobby.getPanel(), BorderLayout.CENTER);
			topFrame.setContentPane(lobby.getPanel());

		} else if (arg0.getSource() == Exit) {
			// could add more prompts and stuff, now it's just abrupt closing
			System.exit(0);
		}
		topFrame.revalidate();
		topFrame.repaint();
	}
}
