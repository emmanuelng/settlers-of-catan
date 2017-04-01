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
import catan.settlers.server.model.Player.ResourceType;

public class MaritimeTradeCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private HashMap<ResourceType, Integer> offer, price;
	private int gameid;

	public MaritimeTradeCommand(HashMap<ResourceType, Integer> offer, HashMap<ResourceType, Integer> price) {
		this.offer = offer;
		this.price = price;
		this.gameid = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
	
		try {
			Game game = server.getGameManager().getGameById(gameid);
			ArrayList<ResourceType> offerList, priceList;
			for( ResourceType r: offer.keySet()){
				offerList.add(r);
			}
			for( ResourceType s: price.keySet()){
				priceList.add(s);
			}
				if(offer.get(r) != null && game.getCurrentPlayer().getResourceAmount(r) > offer.get(r)){
					
					game.getCurrentPlayer().removeResource(r, offer.get(r));
					game.getCurrentPlayer().giveResource(s, price.get(s));
					sender.sendCommand(new UpdateResourcesCommand(game.getCurrentPlayer().getResources()));
				}else{
					sender.sendCommand(new FailureCommand("Not enough resources"));
				}
				
		} catch (IOException e) {
			// Ignore
		}
	}

}
