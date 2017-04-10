package catan.settlers.client.view.game.actions;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;

public class ManageLevelsAction implements GameAction {

	@Override
	public boolean isPossible() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Open flipchart";
	}

	@Override
	public void perform() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if(!gsm.getDoShowFlipchartLayer()){
			gsm.setShowFlipchartLayer(true);
		}else if(gsm.getDoShowFlipchartLayer()){
			gsm.setShowFlipchartLayer(false);
		}
	}

	@Override
	public String getSuccessMessage() {
		return "Manage your Trade, Science and Politics levels";
	}

	@Override
	public String getFailureMessage() {
		return "";
	}

}
