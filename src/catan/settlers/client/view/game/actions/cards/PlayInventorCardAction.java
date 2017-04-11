package catan.settlers.client.view.game.actions.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.PlayProgressCardCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayInventorCardAction implements CardAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if (gsm.getProgressCards().get(ProgressCardType.INVENTOR) > 0) {
			return true;
		}
		return false;

	}

	@Override
	public String getDescription() {
		return "Swap the numbers of two hexagons on the board. They must not be 2, 6, 8, 12.";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		ProgressCardType pcard = ProgressCardType.INVENTOR;
		nm.sendCommand(new PlayProgressCardCommand(pcard));

		int previous = gsm.getProgressCards().get(pcard);
		gsm.getProgressCards().put(pcard, previous - 1);
	}

	@Override
	public ProgressCardType getCardType() {
		return ProgressCardType.INVENTOR;
	}

}
