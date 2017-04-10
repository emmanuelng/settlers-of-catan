package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.CancelJoinGameResponseCommand;
import catan.settlers.network.client.commands.PlayerJoinedGameCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;

public class CancelJoinGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -6249085329263202336L;
	private int gameId;

	public CancelJoinGameCommand(int gameId) {
		this.gameId = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.getPlayersManager().removePlayer(sender.getCredentials());

		try {
			sender.sendCommand(new CancelJoinGameResponseCommand(server.getGameManager().getListOfPublicGames(),
					server.getGameManager().getSavedGames(sender.getCredentials())));
			for (String username : game.getPlayersManager().getParticipantsUsernames()) {
				if (username != sender.getCredentials().getUsername()) {
					Session playerSession = server.getAuthManager().getSessionByUsername(username);
					playerSession.sendCommand(new PlayerJoinedGameCommand(game));
				}
			}
		} catch (IOException e) {
			// Ignore
			e.printStackTrace();
		}
	}

}
