package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class ChooseProgressCardCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 4294718117580041592L;

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowSelectCardTypeMenu(true);
	}

}
