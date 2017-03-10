package catan.settlers.network.client.commands.game;

import catan.settlers.network.client.commands.ServerToClientCommand;

public class PlaceElmtsSetupPhaseOneCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		// TODO Display message on the client
		System.out.println("Place your first Settlement and Road!");
	}
}
