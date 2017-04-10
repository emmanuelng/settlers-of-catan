package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class SelectPlayerToStealFromCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 8039663735773836715L;

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setSelectToSteal(true);
		gsm.setShowSelectPlayerMenu(true);
	}

}
