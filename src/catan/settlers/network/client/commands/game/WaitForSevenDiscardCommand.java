package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class WaitForSevenDiscardCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 8126783461402317960L;
	private int nbOfResponse;
	private int nbOfPlayers;

	public WaitForSevenDiscardCommand(int nbOfResponses, int nbOfPlayers) {
		this.nbOfResponse = nbOfPlayers;
		this.nbOfPlayers = nbOfPlayers;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowSevenDiscardMenu(false);
		gsm.setdBox("A 7 was rolled", "Some players need to discard resources. Please wait (" + nbOfResponse + "/"
				+ nbOfPlayers + " players responded)");
	}

}
