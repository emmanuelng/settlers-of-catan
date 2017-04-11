package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class UpdatePoliticsMetOwnerCommand implements ServerToClientCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9118962424083791876L;
	private String politicsMetOwner;

	public UpdatePoliticsMetOwnerCommand(String PoliticsMetOwner) {
		this.politicsMetOwner = PoliticsMetOwner;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setPolMetOwner(politicsMetOwner);
	}
}
