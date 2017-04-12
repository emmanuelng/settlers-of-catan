package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class FishResourceCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 6712965898194820822L;
	private String playerWhoPlayedCard;

	public FishResourceCommand(String username) {
		this.playerWhoPlayedCard = username;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowFishMenu(false);

		if (playerWhoPlayedCard.equals(ClientModel.instance.getUsername())) {
			gsm.setSelectResourceMessage("Choose the resource you would like");
			gsm.setShowSelectResourceMenuReason("FishResource");
			gsm.setShowSelectResourceMenu(true);
		} else {
			gsm.setdBox(playerWhoPlayedCard + " is drawing a resource", "Please wait.");
		}
	}
}
