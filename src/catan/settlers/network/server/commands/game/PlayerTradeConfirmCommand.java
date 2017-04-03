package catan.settlers.network.server.commands.game;

import java.io.IOException;
import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.game.FailureCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.units.Port.PortKind;

public class PlayerTradeConfirmCommand implements ClientToServerCommand {

	private HashMap<ResourceType, Integer> give, get;
	private Player proposedPlayer;
	private int gameId;
	
	public PlayerTradeConfirmCommand(HashMap<ResourceType, Integer> give, HashMap<ResourceType, Integer> get, Player proposedPlayer){
		this.give = give;
		this.get = get;
		this.proposedPlayer = proposedPlayer;
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
	}
	
	@Override
	public void execute(Session sender, Server server) {
		try{
			Game game = server.getGameManager().getGameById(gameId);
			Player player = game.getCurrentPlayer();
			
			for(ResourceType s: give.keySet()){
				player.removeResource(s, give.get(s));
				proposedPlayer.giveResource(s, give.get(s));
			}
			for(ResourceType t: get.keySet()){
				proposedPlayer.removeResource(t, get.get(t));
				player.giveResource(t, get.get(t));
			}
		
			sender.sendCommand(new UpdateResourcesCommand(player.getResources()));
		}catch (IOException e) {
			// Ignore
		}
	}

}