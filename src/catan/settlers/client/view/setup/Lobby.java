package catan.settlers.client.view.setup;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Lobby implements ActionListener {
	private JButton back;
	private JButton createNewGame;
	private ArrayList<JButton> publicGame; // button for every public game, this
											// will have to be accessed from the
											// server
	private JPanel lobbyPanel;
	private JLabel label1;
	private String user;

	public Lobby(String user) {
		this.user = user;
		lobbyPanel = new JPanel();

		back = new JButton("Back");
		createNewGame = new JButton("Create new game");

		label1 = new JLabel("Public Games");

		lobbyPanel.add(back);
		lobbyPanel.add(createNewGame);
		lobbyPanel.add(label1);

		// populating the publicGame arraylist
		publicGame = new ArrayList<JButton>();
		publicGame.add(new JButton("1"));
		publicGame.add(new JButton("2"));
		publicGame.add(new JButton("3"));

		for (JButton i : publicGame) {
			i = new JButton("Game" + i.getText());
			lobbyPanel.add(i); // add some layout later
		}

		back.addActionListener(this);
		createNewGame.addActionListener(this);
	}

	public JPanel getPanel() {
		return lobbyPanel;
	}

	public void actionPerformed(ActionEvent arg0) {
		JFrame topFrame = MainFrame.getInstance();

		if (arg0.getSource() == back) {
			MainMenu backMenu = new MainMenu(user);
			topFrame.remove(lobbyPanel);
			topFrame.add(backMenu.getPanel());
			topFrame.setContentPane(backMenu.getPanel());
		} else if (arg0.getSource() == createNewGame) {
			NewGame newGame = new NewGame(user);
			topFrame.remove(lobbyPanel);
			topFrame.add(newGame.getPanel());
			topFrame.setContentPane(newGame.getPanel());
		}
		topFrame.revalidate();
		topFrame.repaint();
	}

}
