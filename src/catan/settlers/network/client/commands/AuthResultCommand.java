package catan.settlers.network.client.commands;

public class AuthResultCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private boolean success;
	
	public AuthResultCommand(boolean success) {
		this.success = success;
	}

	@Override
	public void execute() {
		// TODO Manage authentication success/failure on client side
	}

}
