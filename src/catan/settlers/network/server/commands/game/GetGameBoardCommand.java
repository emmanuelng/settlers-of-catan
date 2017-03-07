package catan.settlers.network.server.commands.game;

import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;

public class GetGameBoardCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private int id;

	public GetGameBoardCommand(int gameId) {
		this.id = gameId;
	}

	@Override
	public void execute(Session sender, Server server) {
		try {
			Game game = server.getGameManager().getGameById(id);
			if (game.getPlayersManager().isParticipant(sender.getPlayer())) {
				sender.sendCommand(new UpdateGameBoardCommand(game.getGameBoardManager().getBoard()));
			}
		} catch (Exception e) {
			// Ignore
		}
	}

}
