package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Player.ResourceType;

public class CommercialHarborCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -6429187406767181098L;
	private String currentPlayerUsername;
	private String opponentUsername;
	private ResourceType selectedResource;

	/**
	 * This move has two phases: in the first one the current player selects a
	 * resource, in the second the opponent selects a commodity
	 */

	/**
	 * Constructor for phase one
	 */
	public CommercialHarborCommand(String curPlayerUsername, String oppUsername) {
		this.currentPlayerUsername = curPlayerUsername;
		this.opponentUsername = oppUsername;
		this.selectedResource = null;
	}

	/**
	 * Constructor for phase two
	 */
	public CommercialHarborCommand(String curPlayerUsername, String oppUsername, ResourceType selectedResource) {
		this.currentPlayerUsername = curPlayerUsername;
		this.opponentUsername = oppUsername;
		this.selectedResource = selectedResource;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);
		gsm.setShowSelectResourceMenu(false);
		gsm.setShowCommodityMenu(false);

		if (currentPlayerUsername.equals(ClientModel.instance.getUsername()) && selectedResource == null) {
			// The client is the player who need to select a resource
			gsm.setSelectResourceMessage("Select a resource that you want to exchange with " + opponentUsername);
			gsm.setShowSelectResourceMenu(true);

		} else if (opponentUsername.equals(ClientModel.instance.getUsername()) && selectedResource != null) {
			// The client is the player who currently needs to select a
			// commodity
			gsm.setSelectCommodityMessage(
					"Select a commodity. You will receive 1 " + selectedResource + " from " + currentPlayerUsername);
			gsm.setShowCommodityMenu(true);

		} else {
			// The client doesn't have to interact. Ask him/her to wait
			if (currentPlayerUsername.equals(ClientModel.instance.getUsername())) {
				gsm.setdBox(opponentUsername + " is currently choosing a commodity", "Please wait");
			} else {
				gsm.setdBox(currentPlayerUsername + " played the commercial harbour card",
						"He/she is currently ineracting with " + opponentUsername);
			}

		}

	}

}
