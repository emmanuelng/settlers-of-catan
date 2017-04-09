package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class ResourceMonopolyCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -4491260296692451558L;
	private String playerWhoPlayedCard;

	public ResourceMonopolyCommand(String username) {
		this.playerWhoPlayedCard = username;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (ClientModel.instance.getUsername().equals(playerWhoPlayedCard)) {
			gsm.setShowSelectResourceMenu(true);
		} else {
			gsm.setdBox(playerWhoPlayedCard + " played the Resource monopoly card", "Please wait.");
		}
	}

}
