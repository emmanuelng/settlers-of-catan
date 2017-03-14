package catan.settlers.network.server.commands;

import catan.settlers.network.client.commands.JoinGameResponseCommand;
import catan.settlers.network.client.commands.PlayerJoinedGameCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;

public class JoinGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private int gameId;

	public JoinGameCommand(int gameId) {
		this.gameId = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		try {
			if (game == null) {
				sender.sendCommand(new JoinGameResponseCommand(false, null));
			} else {
				Credentials cred = sender.getCredentials();
				if (game.getPlayersManager().addPlayer(cred)) {
					sender.sendCommand(new JoinGameResponseCommand(true, game));
					notifyOtherPlayers(game, sender, server);

				} else {
					sender.sendCommand(new JoinGameResponseCommand(false, null));
				}
			}
		} catch (Exception e) {
			// Ignore
		}
	}

	private void notifyOtherPlayers(Game game, Session sender, Server server) {
		for (String username : game.getPlayersManager().getParticipantsUsernames()) {
			if (!username.equals(sender.getCredentials().getUsername())) {
				try {
					Session session = server.getAuthManager().getSessionByUsername(username);
					session.sendCommand(new PlayerJoinedGameCommand(game));
				} catch (Exception e) {
					// Ignore
				}
			}
		}
	}

}
