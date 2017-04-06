package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class ChooseProgressCardCommand implements ServerToClientCommand {

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		//TODO: gsm.setShowProgressCardChoiceMenu(true);

	}

}
