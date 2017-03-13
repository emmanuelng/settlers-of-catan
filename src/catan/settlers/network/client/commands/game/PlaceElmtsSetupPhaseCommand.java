package catan.settlers.network.client.commands.game;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.DialogBox;
import catan.settlers.client.view.game.GameWindow;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class PlaceElmtsSetupPhaseCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean isPhaseOne;

	public PlaceElmtsSetupPhaseCommand(boolean isPhaseOne) {
		this.isPhaseOne = isPhaseOne;
	}

	@Override
	public void execute() {
		GameWindow window = ClientWindow.getInstance().getGameWindow();
		DialogBox dbox = new DialogBox("Place your first settlement and road",
				"Select an edge and an intersection and click on \"End Turn\" to confirm");

		if (!isPhaseOne) {
			dbox = new DialogBox("Place your second settlement and road",
					"Select an edge and an intersection and click on \"End Turn\" to confirm");
		}
		window.setDialogBox(dbox);
	}
}
