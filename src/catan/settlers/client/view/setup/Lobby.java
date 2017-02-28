package catan.settlers.client.view.setup;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.GameBoard;
import catan.settlers.network.server.commands.CreateGameCommand;
import catan.settlers.network.server.commands.GetListOfGamesCommand;
import catan.settlers.server.model.Game;

public class Lobby implements ActionListener {
	private JButton back;
	private JButton createNewGame;
	private ArrayList<Game> games; // button for every public game, this
											// will have to be accessed from the
											// server
	private JPanel lobbyPanel;
	private JLabel label1;
	private String user;

	public Lobby(ArrayList<Game> games) {
		this.games = games;
		lobbyPanel = new JPanel();

		back = new JButton("Back");
		createNewGame = new JButton("Create new game");

		label1 = new JLabel("Public Games");

		lobbyPanel.add(back);
		lobbyPanel.add(createNewGame);
		lobbyPanel.add(label1);

		for (int i=0;i<games.size();i++) {
			
			JButton gameButton = new JButton("Game" + i);
			lobbyPanel.add(gameButton); // add some layout later
		}
		// populating the publicGame arraylist
	
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
		} /*else if (arg0.getSource() == createNewGame) {
			NewGame newGame = new NewGame(user);
			topFrame.remove(lobbyPanel);
			topFrame.add(newGame.getPanel());
			topFrame.setContentPane(newGame.getPanel());*/ //reserved for more advanced options of game
		else if(arg0.getSource() == createNewGame){
			//topFrame.getContentPane().removeAll();
			//topFrame.dispose();
		
			ClientModel.instance.sendCommand(new CreateGameCommand(user));
			topFrame.remove(lobbyPanel);
			ClientModel.instance.sendCommand(new GetListOfGamesCommand());
			//GameBoard gameBoard = new GameBoard();
			//topFrame.setTitle("Cattlers of Seten");
		}
		topFrame.revalidate();
		topFrame.repaint();
	}

}
