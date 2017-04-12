package catan.settlers.client.view.game.actions.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.PlayProgressCardCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayMerchantFleetCardAction implements CardAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if (gsm.getProgressCards().get(ProgressCardType.MERCHANT_FLEET) > 0) {
			return true;
		}
		return false;

	}

	@Override
	public String getDescription() {
		return "Select a resource; you may trade that at 2:1 for this turn.";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		nm.sendCommand(new PlayProgressCardCommand(ProgressCardType.MERCHANT_FLEET));
	}

	@Override
	public ProgressCardType getCardType() {
		return ProgressCardType.MERCHANT_FLEET;
	}

}
