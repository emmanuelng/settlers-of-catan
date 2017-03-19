package catan.settlers.network.client.commands.game;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.DialogBox;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class FailureCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private String reasonOfFailure;
	private DialogBox dbox;

	public FailureCommand(String reasonOfFailure) {
		this.reasonOfFailure = reasonOfFailure;
	}
	@Override
	public void execute() {
		dbox = new DialogBox("Failure", reasonOfFailure);
		ClientWindow.getInstance().getGameWindow().getTradeMenu().clear();
		ClientWindow.getInstance().getGameWindow().setDialogBox(dbox);
		ClientWindow.getInstance().getGameWindow().updateWindow();
	}

}
