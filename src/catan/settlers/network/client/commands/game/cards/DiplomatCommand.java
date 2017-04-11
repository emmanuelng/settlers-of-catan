package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class DiplomatCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 8329310401203421805L;
	private String playerWhoPlayerCard;
	private boolean isMovePhase;

	public DiplomatCommand(String username, boolean isMovePhase) {
		this.playerWhoPlayerCard = username;
		this.isMovePhase = isMovePhase;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (playerWhoPlayerCard.equals(ClientModel.instance.getUsername())) {
			if (!isMovePhase) {
				gsm.setdBox("Select a road",
						"If it is an opponent's road, it will be destroyed. If it is yours, you will be able to move it.");
			} else {
				gsm.setdBox("Select a free edge", "You will move your road here.");
			}

			gsm.setShowSelectEdgeLayer(true);
		} else {
			gsm.setdBox(playerWhoPlayerCard + " played the Diplomat card", "Please wait.");
		}
	}

}
