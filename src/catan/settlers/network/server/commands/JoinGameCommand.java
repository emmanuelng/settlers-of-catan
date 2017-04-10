package catan.settlers.network.server.commands;

import catan.settlers.network.client.commands.JoinGameResponseCommand;
import catan.settlers.network.client.commands.PlayerJoinedGameCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.GamePlayersManager.JoinStatus;

public class JoinGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 4220075026895484713L;
	private int gameId;

	public JoinGameCommand(int gameId) {
		this.gameId = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		try {
			if (game == null) {
				sender.sendCommand(new JoinGameResponseCommand(false, "The game does no exist", null));
			} else {
				Credentials cred = sender.getCredentials();
				System.out.println("Joining game...");
				JoinStatus status = game.getPlayersManager().addPlayer(cred);
				System.out.println("Status: " + status);
				if (status == JoinStatus.SUCCESS) {
					sender.sendCommand(new JoinGameResponseCommand(true, "Success!", game));
					notifyOtherPlayers(game, sender, server);
				} else {
					String msg = "";
					switch (status) {
					case ALREADY_JOINED:
						msg = "You already joined the game";
						break;
					case INVALID_GAME_STATUS:
						msg = "The game has already started";
						break;
					case ROOM_FULL:
						msg = "The room is full";
						break;
					default:
						msg = "Impossible to join the game";
						break;
					}
					sender.sendCommand(new JoinGameResponseCommand(false, msg, null));
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
