package catan.settlers.network.client.commands.game.progresscards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.commands.game.progresscards.InventorSecondResponseCommand;
import catan.settlers.server.model.map.Hexagon;

public class InventorSecondCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		int gameId = gsm.getGameId();
		Hexagon selected2 = gsm.getSelectedHex();
		gsm.setdBox("Swap Complete", "The two hex values should be swapped.");
		ClientModel.instance.getNetworkManager().sendCommand(new InventorSecondResponseCommand(selected2,gameId));
		gsm.setSelectedHex(null);

	}

}
