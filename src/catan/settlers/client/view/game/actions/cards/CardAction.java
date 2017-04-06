package catan.settlers.client.view.game.actions.cards;

import catan.settlers.client.view.game.actions.Action;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public interface CardAction extends Action {
	public ProgressCardType getCardType();
}
