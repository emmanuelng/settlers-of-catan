package catan.settlers.network.client.commands;

import java.util.ArrayList;


import catan.settlers.client.view.setup.Lobby;
import catan.settlers.client.view.setup.MainFrame;
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
		Lobby newLobby = new Lobby(games);
		MainFrame.getInstance().add(newLobby.getPanel());
		MainFrame.getInstance().setContentPane(newLobby.getPanel());
		MainFrame.getInstance().revalidate();
		MainFrame.getInstance().repaint();
	}
}