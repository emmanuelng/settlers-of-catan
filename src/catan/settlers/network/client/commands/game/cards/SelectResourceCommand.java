package catan.settlers.network.client.commands.game.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class SelectResourceCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -9198493949651455903L;
	private String reason;

	public SelectResourceCommand(String reason) {
		this.reason = reason;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.doShowSelectResourceMenu();
		gsm.setShowSelectResourceMenuReason(reason);

	}

}
