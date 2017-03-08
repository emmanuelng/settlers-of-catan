package catan.settlers.network.server.commands;

import java.util.ArrayList;

import catan.settlers.network.client.commands.JoinGameResponseCommand;
import catan.settlers.network.client.commands.PlayerJoinedGameCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Game.GamePhase;

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
			if (game == null) { // || game.getPhase() != GamePhase.READYTOJOIN) {
				sender.sendCommand(new JoinGameResponseCommand(false, null, 0));
			} else {
				Player player = sender.getPlayer();
				if (game.getPlayersManager().addPlayer(player)) {
					ArrayList<String> participants = game.getPlayersManager().getParticipantsUsernames();
					sender.sendCommand(new JoinGameResponseCommand(true, participants, game.getGameId()));
					notifyOtherPlayers(game, sender, server);

				} else {
					sender.sendCommand(new JoinGameResponseCommand(false, null, 0));
				}
			}
		} catch (Exception e) {
			// Ignore
		}
	}

	private void notifyOtherPlayers(Game game, Session sender, Server server) {
		PlayerJoinedGameCommand cmd = new PlayerJoinedGameCommand(game.getPlayersManager().getParticipantsUsernames(),
				game.getGameId());

		for (String username : game.getPlayersManager().getParticipantsUsernames()) {
			if (username.equals(sender.getPlayer().getUsername()))
				continue;
			Player curPlayer = server.getPlayerManager().getPlayerByUsername(username);
			Session session = server.getPlayerManager().getSessionByPlayer(curPlayer);
			
			try {
				session.sendCommand(cmd);
			} catch (Exception e) {
				// Ignore
			}
		}
	}

}
