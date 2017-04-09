package catan.settlers.client.view.game.actions.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.PlayProgressCardCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayTradeMonopolyCardAction implements CardAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if (gsm.getProgressCards().get(ProgressCardType.TRADE_MONOPOLY) > 0) {
			return true;
		}
		return false;

	}

	@Override
	public String getDescription() {
		return "Play Trade Monopoly Card";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		nm.sendCommand(new PlayProgressCardCommand(ProgressCardType.TRADE_MONOPOLY));
	}

	@Override
	public ProgressCardType getCardType() {
		return ProgressCardType.TRADE_MONOPOLY;
	}

}
