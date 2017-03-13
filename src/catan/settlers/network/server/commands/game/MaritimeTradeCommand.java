package catan.settlers.network.server.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Player.ResourceType;

public class MaritimeTradeCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private ResourceType rGet, rGive;

	public MaritimeTradeCommand(ResourceType rGet, ResourceType rGive) {
		ClientModel.instance.getCurGameId();
		this.rGet = rGet;
		this.rGive = rGive;
	}

	@Override
	public void execute(Session sender, Server server) {
		//sender.getPlayer().maritimeTrade(rGet, rGive);
	}

}
