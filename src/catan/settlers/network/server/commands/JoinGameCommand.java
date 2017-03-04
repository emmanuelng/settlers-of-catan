package catan.settlers.network.server.commands;

import java.util.ArrayList;

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
		Game game = server.getGameManager().getGameById(gameId);
		try {
			if (game == null) {
				sender.sendCommand(new JoinGameResponseCommand(false, null, 0));
			} else {
				Player player = sender.getPlayer();
				if (game.addPlayer(player)) {
					ArrayList<String> participants = game.getParticipantsUsernames();
					sender.sendCommand(new JoinGameResponseCommand(true, participants, gameId));
				} else {
					sender.sendCommand(new JoinGameResponseCommand(false, null, 0));
				}
			}
		} catch (Exception e) {
			// Ignore
		}
	}

}
