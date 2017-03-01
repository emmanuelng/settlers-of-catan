package catan.settlers.network.server.commands;

import catan.settlers.network.client.commands.JoinGameResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;

public class JoinGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private Game game;

	public JoinGameCommand(Game game) {
		this.game = game;
	}

	@Override
	public void execute(Session sender, Server server) {
		server.writeToConsole("Player " + sender.getPlayer().getUsername() + " wants to join game " + game);
		Player player = sender.getPlayer();

		try {
			if (player == null) {
				sender.sendCommand(new JoinGameResponseCommand(false, game.getParticipants()));
			} else {
				if (game.addPlayer(player)) {
					sender.sendCommand(new JoinGameResponseCommand(true, game.getParticipants()));
				} else {
					sender.sendCommand(new JoinGameResponseCommand(false, game.getParticipants()));
				}
			}
		} catch (Exception e) {
			// Ignore
		}
	}

}
