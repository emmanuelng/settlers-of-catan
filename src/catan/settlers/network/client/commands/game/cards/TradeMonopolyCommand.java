package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class TradeMonopolyCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -4491260296692451558L;
	private String playerWhoPlayedCard;

	public TradeMonopolyCommand(String username) {
		this.playerWhoPlayedCard = username;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (ClientModel.instance.getUsername().equals(playerWhoPlayedCard)) {
			gsm.setShowSelectCommodityMenu(true);
			gsm.setSelectCommodityMessage("You will receive 1 of this commodity from each player that owns it");
		} else {
			gsm.setdBox(playerWhoPlayedCard + " played the Trade monopoly card", "Please wait.");
		}

	}

}
