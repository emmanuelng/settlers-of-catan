package catan.settlers.network.client.commands;

import java.util.ArrayList;


import catan.settlers.client.view.setup.Lobby;
import catan.settlers.client.view.setup.ClientWindow;
import catan.settlers.server.model.Game;

public class GameListResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private ArrayList<Game> games;

	public GameListResponseCommand(ArrayList<Game> games) {
		this.games = games;
	}

	@Override
	public void execute() {
		System.out.println(games);
		ClientWindow.getInstance().setScreen(new Lobby(games));
	}
}
