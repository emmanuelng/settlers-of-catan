package catan.settlers.network.server.commands;

import catan.settlers.network.client.commands.JoinGameResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.Game;

public class CreateGameCommand implements ClientToServerCommand {

	private static final long serialVersionUID = -3417133778660916568L;

	@Override
	public void execute(Session sender, Server server) {
		try {
			Game game = server.getGameManager().createGame(sender.getCredentials());
			sender.sendCommand(new JoinGameResponseCommand(true, "Success!", game));
			int nbOfGames = server.getGameManager().getListOfPublicGames().size();
			server.writeToConsole("New game created. There are now " + nbOfGames + " games");
		} catch (Exception e) {
			// Ignore
			e.printStackTrace();
		}
	}

}
