package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;

public class ExitMoveKnightMode implements GameAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		return gsm.isMoveKnightMode();
	}

	@Override
	public String getDescription() {
		return "Cancel";
	}

	@Override
	public void perform() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setMoveKnightMode(false);
	}

	@Override
	public String getSuccessMessage() {
		return "Exit move knight mode";
	}

	@Override
	public String getFailureMessage() {
		return "";
	}

}
