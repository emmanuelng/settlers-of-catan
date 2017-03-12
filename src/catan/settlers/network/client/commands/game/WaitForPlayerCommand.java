package catan.settlers.network.client.commands.game;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.DialogBox;
import catan.settlers.client.view.game.GameWindow;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class WaitForPlayerCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private String username;

	public WaitForPlayerCommand(String username) {
		this.username = username;
	}

	@Override
	public void execute() {
		// TODO Display message on the client
		// Also disable buttons? Or have a myTurn state?
		GameWindow window = ClientWindow.getInstance().getGameWindow();
		DialogBox dbox = new DialogBox("Waiting for " + username, "Please wait until the turn is done");
		window.setDialogBox(dbox);
	}

}
