package catan.settlers.network.client.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class ReceiveTradeRequestCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private HashMap<ResourceType, Integer> give,get;
	private Player player;

	public ReceiveTradeRequestCommand(HashMap<ResourceType, Integer> give, HashMap<ResourceType, Integer> get, Player proposedPlayer){
		this.give = give;
		this.get = get;
		this.player = proposedPlayer;
	}
	
	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setShowTradeReceivedMenu(true);
		ClientModel.instance.getGameStateManager().setReceivedTradeOffer(get, give, player);
	}

}
