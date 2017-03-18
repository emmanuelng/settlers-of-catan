package catan.settlers.network.client.commands;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;

public class StartGameCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().sync();
		ClientWindow.getInstance().switchToGame();
	}

}
