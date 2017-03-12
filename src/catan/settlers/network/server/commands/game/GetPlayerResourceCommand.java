package catan.settlers.network.server.commands.game;

import java.io.IOException;

import catan.settlers.network.client.commands.game.PlayerResourceResponseCommand;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Player.ResourceType;

public class GetPlayerResourceCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;

	public GetPlayerResourceCommand(int gameId) {
	}

	@Override
	public void execute(Session sender, Server server) {
		int g = sender.getPlayer().getResourceAmount(ResourceType.GRAIN);
		int l = sender.getPlayer().getResourceAmount(ResourceType.LUMBER);
		int o = sender.getPlayer().getResourceAmount(ResourceType.ORE);
		int w = sender.getPlayer().getResourceAmount(ResourceType.WOOL);
		int b = sender.getPlayer().getResourceAmount(ResourceType.BRICK);
		int c = sender.getPlayer().getResourceAmount(ResourceType.CLOTH);
		int p = sender.getPlayer().getResourceAmount(ResourceType.PAPER);
		int co = sender.getPlayer().getResourceAmount(ResourceType.COIN);

		try {
			sender.sendCommand(new PlayerResourceResponseCommand(g, l, o, w, b, c, p, co));
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

}
