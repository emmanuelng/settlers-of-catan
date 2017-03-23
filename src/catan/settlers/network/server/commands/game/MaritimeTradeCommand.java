package catan.settlers.network.server.commands.game;

import java.io.IOException;
import java.util.ArrayList;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.game.FailureCommand;
import catan.settlers.network.client.commands.game.UpdatePlayerResourcesCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player.ResourceType;

public class MaritimeTradeCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private ArrayList<ResourceType> rGet, rGive;
	private int gameid;
	private int valueToRemove;

	public MaritimeTradeCommand(ArrayList<ResourceType> rGet, ArrayList<ResourceType> rGive, int valueToRemove) {
		this.rGet = rGet;
		this.rGive = rGive;
		this.gameid = ClientModel.instance.getGameStateManager().getGameId();
		this.valueToRemove = valueToRemove;
	}

	@Override
	public void execute(Session sender, Server server) {
	
		try {
			Game game = server.getGameManager().getGameById(gameid);
			server.writeToConsole("" + game.getCurrentPlayer().getResourceAmount(rGive.get(0)));
			if(rGive.get(0) != null && game.getCurrentPlayer().getResourceAmount(rGive.get(0))>valueToRemove){
				server.writeToConsole("executed");
				game.getCurrentPlayer().removeResource(rGive.get(0), valueToRemove);
				game.getCurrentPlayer().giveResource(rGet.get(0), 1);
				sender.sendCommand(new UpdatePlayerResourcesCommand(game.getCurrentPlayer().getResources()));
			}else{
				sender.sendCommand(new FailureCommand("Not enough resources"));
			}
		} catch (IOException e) {
			// Ignore
		}
	}

}
