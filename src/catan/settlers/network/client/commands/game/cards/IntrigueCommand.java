package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.units.Knight;

public class IntrigueCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -3379894570760765134L;
	private String playerWhoPlayedCard;
	private Knight selectedKnight;

	public IntrigueCommand(String username) {
		this.playerWhoPlayedCard = username;
		this.selectedKnight = null;
	}

	public IntrigueCommand(String username, Knight knight) {
		this.playerWhoPlayedCard = username;
		this.selectedKnight = knight;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (playerWhoPlayedCard.equals(ClientModel.instance.getUsername())) {
			if (selectedKnight == null) {
				gsm.setdBox("Select an opponent knight to move", "This knight must be on one of your road networks");
				gsm.setShowSelectIntersectionLayer(true);
			} else {
				gsm.setdBox("Move the knight", "Select one of the green intersections");
				gsm.setCanMoveKnightIntersecIds(selectedKnight.canCanMoveIntersecIds());
				gsm.setShowSelectIntersectionLayer(true);
				gsm.setMoveKnightMode(true);
			}
		} else {
			gsm.setdBox(playerWhoPlayedCard + " played the Intrigue card!", "Please wait.");
		}
	}

}
