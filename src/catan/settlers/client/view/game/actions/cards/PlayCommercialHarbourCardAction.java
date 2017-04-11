package catan.settlers.client.view.game.actions.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.PlayProgressCardCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayCommercialHarbourCardAction implements CardAction {

	@Override
	public boolean isPossible() {

		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if (gsm.getProgressCards().get(ProgressCardType.COMMERCIAL_HARBOR) > 0) {
			return true;
		}
		return false;

	}

	@Override
	public String getDescription() {
		return "Give each opponent a resource of your choice in exchange for a commodity of their choice.";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		nm.sendCommand(new PlayProgressCardCommand(ProgressCardType.COMMERCIAL_HARBOR));
	}

	@Override
	public ProgressCardType getCardType() {
		return ProgressCardType.COMMERCIAL_HARBOR;
	}

}
