package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.DialogBox;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class FailureCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private String reasonOfFailure;

	public FailureCommand(String reasonOfFailure) {
		this.reasonOfFailure = reasonOfFailure;
	}

	@Override
	public void execute() {
		ClientWindow.getInstance().getGameWindow().getTradeMenu().clear();
		ClientModel.instance.getGameStateManager().setdBox("Failure", reasonOfFailure);
	}

}
