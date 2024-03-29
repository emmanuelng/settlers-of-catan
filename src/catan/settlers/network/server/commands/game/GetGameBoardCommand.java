package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;

public class GetGameBoardCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 3205599258314591002L;
	private int gameId;

	public GetGameBoardCommand() {
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
		try {
			Game game = server.getGameManager().getGameById(gameId);
			if (game.getPlayersManager().isParticipant(sender.getCredentials())) {
				sender.sendCommand(new UpdateGameBoardCommand(game.getGameBoardManager().getBoard()));
			}
		} catch (Exception e) {
			// Ignore
		}
	}

}
