package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class DiscardCardsCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -7093642228616819458L;

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowSevenDiscardMenu(true);
	}

}
