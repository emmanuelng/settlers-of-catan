package catan.settlers.network.client.commands;

public class JoinGameResponseCommand implements ServerToClientCommand {
	
	private boolean success;
	
	public JoinGameResponseCommand(boolean success) {
		this.success = success;
	}
	
	@Override
	public void execute() {
		// TODO Handle Join game result here		
	}
}
