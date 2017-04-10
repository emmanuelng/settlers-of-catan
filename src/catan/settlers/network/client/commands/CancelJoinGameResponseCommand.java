package catan.settlers.network.client.commands;

import java.util.ArrayList;

import catan.settlers.client.model.GameRepresentation;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.setup.Lobby;
import catan.settlers.server.model.Game;

public class CancelJoinGameResponseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -7349847598684566394L;
	private ArrayList<GameRepresentation> publicGames;
	private ArrayList<GameRepresentation> savedGames;

	public CancelJoinGameResponseCommand(ArrayList<Game> publicGames, ArrayList<Game> savedGames) {
		this.publicGames = new ArrayList<>();
		this.savedGames = new ArrayList<>();

		for (Game game : publicGames)
			this.publicGames.add(new GameRepresentation(game));

		for (Game game : savedGames)
			this.savedGames.add(new GameRepresentation(game));

	}

	@Override
	public void execute() {
		ClientWindow.getInstance().getSetupWindow().setScreen(new Lobby(publicGames, savedGames));
	}
}
