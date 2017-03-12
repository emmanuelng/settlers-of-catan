package catan.settlers.network.client.commands.game;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.DialogBox;
import catan.settlers.client.view.game.GameWindow;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class PlaceElmtsSetupPhaseOneCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		GameWindow window = ClientWindow.getInstance().getGameWindow();
		DialogBox dbox = new DialogBox("Place your first settlement and road",
				"Select an edge and an intersection and click on \"End Turn\" to confirm");
		window.setDialogBox(dbox);
	}
}
