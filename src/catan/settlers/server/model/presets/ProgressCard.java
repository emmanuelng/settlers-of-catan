package catan.settlers.server.model.presets;

import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class ProgressCard {

	public ProgressCard(Game game) {

		for (Player p : game.getParticipants()) {
			for (ProgressCardType ptype : ProgressCardType.values()) {
				p.giveProgressCard(ptype);
			}
		}
	}

}
