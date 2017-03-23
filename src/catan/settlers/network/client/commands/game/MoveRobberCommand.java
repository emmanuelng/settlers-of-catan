package catan.settlers.network.client.commands.game;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.DialogBox;
import catan.settlers.client.view.game.GameWindow;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class MoveRobberCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		GameWindow window = ClientWindow.getInstance().getGameWindow();
		DialogBox dbox = new DialogBox("Select a hexagon to block with the robber",
				"Also select a settlement/city to steal a random resource from.");
		window.setDialogBox(dbox);
	}

}
