package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class FailureCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private String reasonOfFailure;

	public FailureCommand(String reasonOfFailure) {
		this.reasonOfFailure = reasonOfFailure;
	}

	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setdBox("Failure", reasonOfFailure);
	}

}
