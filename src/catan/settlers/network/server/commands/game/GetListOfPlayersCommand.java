package catan.settlers.network.server.commands.game;

import java.io.IOException;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.game.PlayerListResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;

public class GetListOfPlayersCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private int gameID;

	public GetListOfPlayersCommand() {
		gameID = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
		try {
			Game game = server.getGameManager().getGameById(gameID);
			sender.sendCommand(new PlayerListResponseCommand(game.getPlayersManager().getParticipantsUsernames()));
		} catch (IOException e) {
			// Ignore
		}
	}

}
