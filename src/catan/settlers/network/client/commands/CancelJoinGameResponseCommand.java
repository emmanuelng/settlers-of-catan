package catan.settlers.network.client.commands;

import java.util.ArrayList;

import catan.settlers.client.model.GameRepresentation;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.setup.Lobby;
import catan.settlers.server.model.Game;

public class CancelJoinGameResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private ArrayList<GameRepresentation> games;

	public CancelJoinGameResponseCommand(ArrayList<Game> games) {
		this.games = new ArrayList<>();

		for (Game game : games) {
			this.games.add(new GameRepresentation(game));
		}
	}

	@Override
	public void execute() {
		ClientWindow.getInstance().getSetupWindow().setScreen(new Lobby(games));
	}
}
