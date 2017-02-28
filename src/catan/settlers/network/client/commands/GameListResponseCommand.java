package catan.settlers.network.client.commands;

import java.util.ArrayList;

import javax.swing.JButton;

import catan.settlers.server.model.Game;

public class GameListResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private ArrayList<Game> games;

	public GameListResponseCommand(ArrayList<Game> games) {
		this.games = games;
	}

	@Override
	public void execute() {
		// TODO Display the list of games
		for (game i : games) {
			i = new JButton("Game" + i.getText());
			lobbyPanel.add(i); // add some layout later
		}
	}
}
