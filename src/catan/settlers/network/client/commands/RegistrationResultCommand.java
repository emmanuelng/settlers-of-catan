package catan.settlers.network.client.commands;

public class RegistrationResultCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean success;
	
	public RegistrationResultCommand(boolean success) {
		this.success = success;
	}

	@Override
	public void execute() {
		// TODO Manage registration success/failure on client side
	}

}
