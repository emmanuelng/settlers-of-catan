package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class AqueductCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5382590573448177294L;
	private String playerWithAqueduct;

	public AqueductCommand(String username) {
		this.playerWithAqueduct = username;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();

		if (ClientModel.instance.getUsername().equals(playerWithAqueduct)) {
			gsm.setSelectResourceMessage("Choose the resource you would like");
			gsm.setShowSelectResourceMenu(true);
			gsm.setShowSelectResourceMenuReason("Aqueduct");
		} else {
			gsm.setdBox(playerWithAqueduct + " is choosing the resource through Aqueduct", "Please wait");
		}

	}

}
