package catan.settlers.client.view.game.actions.progresscards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.client.view.game.actions.Action;
import catan.settlers.network.server.commands.game.PlayProgressCardCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayInventorCardAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if(gsm.getProgressCards().get(ProgressCardType.INVENTOR) > 0){
			return true;
		}
		return false;
			
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Play Inventor Card";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		ProgressCardType pcard = ProgressCardType.INVENTOR;
		nm.sendCommand(new PlayProgressCardCommand(pcard));
		
		int previous = gsm.getProgressCards().get(pcard);
		gsm.getProgressCards().put(pcard, previous-1);
	}

}
