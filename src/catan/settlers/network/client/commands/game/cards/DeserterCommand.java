package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class DeserterCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -1978675627362831628L;
	private String playerWhoPlayedCard;

	public DeserterCommand(String username) {
		this.playerWhoPlayedCard = username;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (playerWhoPlayedCard.equals(ClientModel.instance.getUsername())) {
			gsm.setShowSelectIntersectionLayer(true);
			gsm.setdBox("Select an opponent's knight", "This knight will be yours");
		} else {
			gsm.setdBox(playerWhoPlayedCard + " played the Deserter card", "Please wait.");
		}
	}

}
