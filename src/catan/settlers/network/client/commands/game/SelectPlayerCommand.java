package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.GameStateManager.SelectionReason;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class SelectPlayerCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 6324859214229351576L;
	SelectionReason reason;

	public SelectPlayerCommand(SelectionReason reason) {
		this.reason = reason;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setSelectionReason(reason);
		gsm.setShowSelectPlayerMenu(true);
	}

}
