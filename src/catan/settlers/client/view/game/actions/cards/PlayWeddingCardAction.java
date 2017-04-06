package catan.settlers.client.view.game.actions.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.PlayProgressCardCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayWeddingCardAction implements CardAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if (gsm.getProgressCards().get(ProgressCardType.WEDDING) > 0) {
			return true;
		}
		return false;

	}

	@Override
	public String getDescription() {
		return "Play Wedding Card";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		ProgressCardType pcard = ProgressCardType.WEDDING;
		nm.sendCommand(new PlayProgressCardCommand(pcard));

		int previous = gsm.getProgressCards().get(pcard);
		gsm.getProgressCards().put(pcard, previous - 1);
	}

	@Override
	public ProgressCardType getCardType() {
		return ProgressCardType.WEDDING;
	}

}
