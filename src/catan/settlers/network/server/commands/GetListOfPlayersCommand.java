package catan.settlers.network.server.commands;

import java.io.IOException;

import catan.settlers.network.client.commands.PlayerListResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;

public class GetListOfPlayersCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private int gameID;

	public GetListOfPlayersCommand(int gameId) {
		gameID = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		// TODO Auto-generated method stub
		try {
			System.out.println(
					server.getGameManager().getGameById(gameID).getPlayersManager().getParticipantsUsernames());
			sender.sendCommand(new PlayerListResponseCommand(
					server.getGameManager().getGameById(gameID).getPlayersManager().getParticipantsUsernames()));
		} catch (IOException e) {
			// Ignore
		}
	}

}
