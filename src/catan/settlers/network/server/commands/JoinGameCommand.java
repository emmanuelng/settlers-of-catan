package catan.settlers.network.server.commands;

import catan.settlers.network.client.commands.JoinGameResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;

public class JoinGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private String username;
	private Game game;

	public JoinGameCommand(Game game, String username) {
		this.game = game;
		this.username = username;
	}

	@Override
	public void execute(Session sender, Server server) {
		server.writeToConsole("Player " + username + " wants to join game " + game);
		Player player = server.getPlayerManager().getPlayerByUsername(username);

		try {
			if (player == null) {
				sender.sendCommand(new JoinGameResponseCommand(false));
			} else {
				if (game.addPlayer(player)) {
					sender.sendCommand(new JoinGameResponseCommand(true));
				} else {
					sender.sendCommand(new JoinGameResponseCommand(false));
				}
			}
		} catch (Exception e) {
			// Ignore
		}
	}

}
