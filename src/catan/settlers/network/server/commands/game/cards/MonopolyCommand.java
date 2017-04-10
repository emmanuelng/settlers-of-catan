package catan.settlers.network.server.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.GamePlayersManager;

public class MonopolyCommand implements ClientToServerCommand {

	private ResourceType selected;
	private int gameId;
	
	public MonopolyCommand(ResourceType res) {
		selected = res;
		gameId = ClientModel.instance.getGameStateManager().getGameId();
	}
	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		Player cardPlayer = game.getPlayersManager().getPlayerByCredentials(server.getAuthManager().getCredentialsBySession(sender));
		int amtToGive = 0;
		if (selected == ResourceType.CLOTH || selected == ResourceType.COIN || selected == ResourceType.PAPER) {
			for (Player p : game.getParticipants()) {
				if (p != cardPlayer) {
					if (p.getResourceAmount(selected) > 0) {
						p.removeResource(selected, 1);
						amtToGive++;
					}
				}		
			}
			cardPlayer.giveResource(selected, amtToGive);
		} else {
			for (Player p : game.getParticipants()) {
				if (p != cardPlayer) {
					if (p.getResourceAmount(selected) > 1) {
						p.removeResource(selected, 2);
						amtToGive += 2;
					} else if (p.getResourceAmount(selected) > 0) {
						p.removeResource(selected, 1);
						amtToGive++;
					}
				}		
			}
			cardPlayer.giveResource(selected, amtToGive);
		}
	}

}
