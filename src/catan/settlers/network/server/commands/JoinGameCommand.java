package catan.settlers.network.server.commands;

import catan.settlers.network.client.commands.JoinGameResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;

public class JoinGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private int gameId;

	public JoinGameCommand(int gameId) {
		this.gameId = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		Player player = sender.getPlayer();
		Game game = server.getGameManager().getGameById(gameId);

		server.writeToConsole("Player " + sender.getPlayer().getUsername() + " wants to join game " + game);

		try {
			if (player == null || game == null) {
				sender.sendCommand(new JoinGameResponseCommand(false, game.getParticipantsUsernames(), game.getGameId()));
			} else {
				if (game.addPlayer(player)) {
					sender.sendCommand(new JoinGameResponseCommand(true, game.getParticipantsUsernames(), game.getGameId()));
				} else {
					sender.sendCommand(new JoinGameResponseCommand(false, game.getParticipantsUsernames(), game.getGameId()));
				}
			}
		} catch (Exception e) {
			// Ignore
			e.printStackTrace();
		}
	}

}
