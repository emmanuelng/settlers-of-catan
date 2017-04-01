package catan.settlers.network.server.commands.game;

import java.io.IOException;
import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Player.ResourceType;

public class PlayerTradeCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private HashMap<ResourceType, Integer> offer, price;
	private int gameId;

	public PlayerTradeCommand(HashMap<ResourceType, Integer> offer, HashMap<ResourceType, Integer> price) {
		this.offer = offer;
		this.price = price;
		this.gameId = ClientModel.instance.getGameStateManager().getGameId();
	}

	@Override
	public void execute(Session sender, Server server) {
	}

}
