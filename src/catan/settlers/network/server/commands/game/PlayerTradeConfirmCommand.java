package catan.settlers.network.server.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.game.TradeSuccessCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class PlayerTradeConfirmCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1963406279320893958L;
	private HashMap<ResourceType, Integer> give, get;
	private int gameId;

	public PlayerTradeConfirmCommand(HashMap<ResourceType, Integer> give, HashMap<ResourceType, Integer> get,
			Player proposedPlayer) {
		this.give = give;
		this.get = get;
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
		Game game = server.getGameManager().getGameById(gameId);
		Player personWhoPropose = game.getCurrentPlayer();
		Credentials personWhoAcceptsCred = sender.getCredentials();
		Player personWhoAccepts = game.getPlayersManager().getPlayerByCredentials(personWhoAcceptsCred);

		for (ResourceType rtype : get.keySet()) {
			personWhoAccepts.giveResource(rtype, give.get(rtype));
			personWhoPropose.removeResource(rtype, give.get(rtype));
		}

		for (ResourceType rtype : give.keySet()) {
			personWhoAccepts.removeResource(rtype, get.get(rtype));
			personWhoPropose.giveResource(rtype, get.get(rtype));
		}

		for (Player p : game.getParticipants()) {
			p.sendCommand(new UpdateResourcesCommand(p.getResources()));
			p.sendCommand(new TradeSuccessCommand(personWhoPropose.getUsername()));
		}
	}

}
