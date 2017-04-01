package catan.settlers.network.server.commands.game;

import java.io.IOException;
import java.util.ArrayList;
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

public class MaritimeTradeCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private HashMap<ResourceType, Integer> give, get;
	private int gameid;

	public MaritimeTradeCommand(HashMap<ResourceType, Integer> give, HashMap<ResourceType, Integer> get) {
		this.give = give;
		this.get = get;
		this.gameid = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
	
		try {
			Game game = server.getGameManager().getGameById(gameid);
			Player player = game.getCurrentPlayer();

			
			
			for(ResourceType s: give.keySet()){
				if(give.get(s)>=2){
					if(player.hasPortOfThisResource(s) && give.get(s)==2){
						player.removeResource(s, 2);
					}
					else if(player.hasPort(PortKind.ALLPORT) && give.get(s)==3){
						player.removeResource(s, 3);
					}else if(give.get(s)==4){
						player.removeResource(s, 4);
					}
					for(ResourceType r: get.keySet()){
						if(get.get(r) > 1 ){
							sender.sendCommand(new FailureCommand("Please only trade for one resource."));
							return;
						}else if(get.get(r)==1){
							player.giveResource(r, 1);
						}
					}
				}
			}
			
			sender.sendCommand(new UpdateResourcesCommand(player.getResources()));
		} catch (IOException e) {
			// Ignore
		}
	}

}
