package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class WaitForSetOfOpponentMoveCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 2460528275812534933L;
	private int nbOfResponse;
	private int nbOfPlayers;
	private String reason;

	public WaitForSetOfOpponentMoveCommand(int nbOfResponses, int nbOfPlayers, String reason) {
		this.nbOfResponse = nbOfPlayers;
		this.nbOfPlayers = nbOfPlayers;
		this.reason = reason;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowSevenDiscardMenu(false);

		if (reason.equals("SevenDiscard")) {
			gsm.setdBox("A 7 was rolled", "Some players need to discard resources. Please wait (" + nbOfResponse + "/"
					+ nbOfPlayers + " players responded)");
		} else if (reason.equals("CommercialHarbour")) {
			gsm.setdBox("Somebody played a commercial harbour card",
					"Everybody needs to choose a resource/commodity " + nbOfPlayers + " players responded");
		}

	}

}
