package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class WeddingCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 5210973154000774924L;
	private String playerWhoPlayedCard;
	private boolean mustPlay;

	public WeddingCommand(String username, boolean mustPlay) {
		this.playerWhoPlayedCard = username;
		this.mustPlay = mustPlay;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (mustPlay) {
			gsm.setShowSelectResourceMenu(true);
		} else {
			if (ClientModel.instance.getUsername().equals(playerWhoPlayedCard))
				gsm.setdBox("You played the Wedding card", "Please wait.");
			else
				gsm.setdBox(playerWhoPlayedCard + " played the Wedding card", "Please wait.");
		}

	}

}
