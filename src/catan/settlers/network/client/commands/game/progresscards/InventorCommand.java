package catan.settlers.network.client.commands.game.progresscards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.commands.game.progresscards.InventorFirstResponseCommand;
import catan.settlers.server.model.map.Hexagon;

public class InventorCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		int gameId = gsm.getGameId();
		Hexagon selected1 = gsm.getSelectedHex();
		gsm.setdBox("Selection Made", "Now select the hex to swap with.");
		ClientModel.instance.getNetworkManager().sendCommand(new InventorFirstResponseCommand(selected1,gameId));
		gsm.setSelectedHex(null);
	}

}
