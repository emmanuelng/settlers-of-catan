package catan.settlers.network.server.commands.game;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.map.Intersection;

public class BuildSettlementCommand implements ClientToServerCommand {
	
	private Intersection intersection;
	private int gameId;
	
	public BuildSettlementCommand(int gameId, Intersection intersection) {
		this.gameId = gameId;
		this.intersection = intersection;
	}

	@Override
	public void execute(Session sender, Server server) {
		System.out.println("Player wants to build a settlement");
	}
	
	
}
