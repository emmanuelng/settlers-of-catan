package catan.settlers.network.client.commands.game.cards;

import java.util.ArrayList;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Player.ResourceType;

public class MerchantFleetCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -4302736733306996279L;
	private String playerWhoPlayedTheCard;
	private ArrayList<ResourceType> resourcesWithAdvantage;

	public MerchantFleetCommand(String username) {
		this.playerWhoPlayedTheCard = username;
		this.resourcesWithAdvantage = null;
	}

	public MerchantFleetCommand(String username, ArrayList<ResourceType> resources) {
		this.playerWhoPlayedTheCard = username;
		this.resourcesWithAdvantage = resources;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (ClientModel.instance.getUsername().equals(playerWhoPlayedTheCard)) {
			if (resourcesWithAdvantage == null) {
				gsm.setSelectResourceMessage("Select a card that you want to trade at 2:1 for this turn");
				gsm.setShowSelectResourceMenu(true);
			} else {
				gsm.setMerchantFleetAdvantage(resourcesWithAdvantage);
			}
		} else {
			gsm.setdBox(playerWhoPlayedTheCard + " played the Merchant fleet card!", "Please wait");
		}

	}

}
