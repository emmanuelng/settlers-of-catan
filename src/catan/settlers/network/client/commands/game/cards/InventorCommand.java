package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class InventorCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -4213963669926891686L;
	private String playerWhoPlayedCard;

	public InventorCommand(String username) {
		this.playerWhoPlayedCard = username;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (playerWhoPlayedCard.equals(ClientModel.instance.getUsername())) {
			gsm.setdBox("Select an hexagon",
					"You need to select two intersections in total. If you already selected one hexagon, please select the second one.");
			gsm.doShowSelectHexLayer(true);
		} else {
			gsm.setdBox(playerWhoPlayedCard + " played the Inventor card", "Please wait.");
		}
	}

}
