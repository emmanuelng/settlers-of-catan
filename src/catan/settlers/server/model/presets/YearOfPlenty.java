package catan.settlers.server.model.presets;

import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class YearOfPlenty {

	public YearOfPlenty(Game game) {

		for (Player p : game.getParticipants()) {
			for (ResourceType rtype : ResourceType.values()) {
				p.giveResource(rtype, 15);
			}
			
			p.giveFish(50);
		}
	}

}
