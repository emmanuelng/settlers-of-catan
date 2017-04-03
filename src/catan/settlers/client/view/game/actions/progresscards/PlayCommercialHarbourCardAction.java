package catan.settlers.client.view.game.actions.progresscards;


import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.game.actions.Action;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayCommercialHarbourCardAction implements Action {

	@Override
	public boolean isPossible() {

		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if(gsm.getProgressCards().get(ProgressCardType.COMMERCIAL_HARBOR) > 0){
			return true;
		}
		return false;
			
	}

	@Override
	public String getDescription() {
		return "Play Commercial Harbour Card";
	}

	@Override
	public void perform() {
		
	}

}
