package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;

public class MoveRobberAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if(gsm.getAttacked()){
			return gsm.getSelectedHex() != null && gsm.canMoveRobber();
		}else{
			return false;
		}
	}

	@Override
	public String getDescription() {
		return "Move robber";
	}

	@Override
	public void perform() {
		System.out.println(this);
	}

}
