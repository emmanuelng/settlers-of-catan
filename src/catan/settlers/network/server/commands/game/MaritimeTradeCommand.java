package catan.settlers.network.server.commands.game;

import java.io.IOException;
import java.util.ArrayList;

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

	public MaritimeTradeCommand(ArrayList<ResourceType> rGet, ArrayList<ResourceType> rGive, int gameid) {
		this.rGet = rGet;
		this.rGive = rGive;
		this.gameid = gameid;
	}

	@Override
	public void execute(Session sender, Server server) {
	
		try {
			Game game = server.getGameManager().getGameById(gameid);
			System.out.println(rGet);
			if(rGive.get(0) != null){
				game.getCurrentPlayer().removeResource(rGive.get(0), 1);
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
