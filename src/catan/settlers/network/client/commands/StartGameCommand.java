package catan.settlers.network.client.commands;

import catan.settlers.client.view.ClientWindow;

public class StartGameCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		ClientWindow.getInstance().switchToGame();
	}

}
