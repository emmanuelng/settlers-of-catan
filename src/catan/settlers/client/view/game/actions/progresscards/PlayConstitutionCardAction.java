package catan.settlers.client.view.game.actions.progresscards;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.client.view.game.actions.Action;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.network.server.commands.game.PlayProgressCardCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class PlayConstitutionCardAction implements Action {

	@Override
	public boolean isPossible() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		if(gsm.getProgressCards().get(ProgressCardType.CONSTITUTION) > 0){
			return true;
		}
		return false;
			
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Play Constitution Card";
	}

	@Override
	public void perform() {
		// TODO Auto-generated method stub
		NetworkManager nm = ClientModel.instance.getNetworkManager();
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		nm.sendCommand(new PlayProgressCardCommand(ProgressCardType.CONSTITUTION));
		
		int previous = gsm.getProgressCards().get(ProgressCardType.CONSTITUTION);
		gsm.getProgressCards().put(ProgressCardType.CONSTITUTION, previous-1);
		
	}

}
