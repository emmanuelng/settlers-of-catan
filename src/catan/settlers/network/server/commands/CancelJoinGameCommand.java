package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.CancelJoinGameResponseCommand;
import catan.settlers.network.client.commands.PlayerJoinedGameCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;

public class CancelJoinGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;

	private int gameId;

	public CancelJoinGameCommand(int gameId) {
		this.gameId = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		game.getPlayersManager().removePlayer(sender.getPlayer());

		try {
			sender.sendCommand(new CancelJoinGameResponseCommand(server.getGameManager().getListOfGames()));
			for (String username : game.getPlayersManager().getParticipantsUsernames()) {
				Player curPlayer = server.getPlayerManager().getPlayerByUsername(username);
				if (curPlayer != sender.getPlayer()) {
					curPlayer.sendCommand(new PlayerJoinedGameCommand(
							game.getPlayersManager().getParticipantsUsernames(), game.getGameId(),
							game.getPlayersManager().getNbOfReadyPlayers(), Game.MAX_NB_OF_PLAYERS));
				}
			}
		} catch (IOException e) {
			// Ignore
		}
	}

}
