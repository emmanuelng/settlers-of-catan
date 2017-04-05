package catan.settlers.client.view.game.actions.cards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.network.server.commands.game.PlayProgressCardCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayBishopCardAction implements CardAction {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if(gsm.getProgressCards().get(ProgressCardType.BISHOP) > 0){
			if(gsm.getAttacked()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Play Bishop Card";
	}

	@Override
	public void perform() {
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		nm.sendCommand(new PlayProgressCardCommand(ProgressCardType.BISHOP));
		
		int previous = gsm.getProgressCards().get(ProgressCardType.BISHOP);
		gsm.getProgressCards().put(ProgressCardType.BISHOP, previous-1);

	}

}
