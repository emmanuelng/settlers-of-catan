package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class BishopCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -7322432622946052233L;
	private String playerWhoPlayedCard;

	public BishopCommand(String username) {
		this.playerWhoPlayedCard = username;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (ClientModel.instance.getUsername().equals(playerWhoPlayedCard)) {
			gsm.setdBox("Select an Hexagon", "You will receive one resource from each player on this hexagon");
			gsm.doShowSelectHexLayer(true);
		} else {
			gsm.setdBox(playerWhoPlayedCard + " played the Bishop card", "Please wait.");
		}
	}

}
