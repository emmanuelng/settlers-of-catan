package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;

public class ExitGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -5089672393040592156L;
	private int gameId;

	public ExitGameCommand() {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.getPlayersManager().sessionWasClosed(sender.getCredentials());
	}

}
