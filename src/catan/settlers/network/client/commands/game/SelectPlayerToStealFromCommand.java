package catan.settlers.network.client.commands.game;

import java.util.ArrayList;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.GameStateManager.SelectionReason;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Player;

public class SelectPlayerToStealFromCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 8039663735773836715L;
	private boolean fish;
	private ArrayList<String> victims;
	
	public SelectPlayerToStealFromCommand(){
		fish = true;
	}
	
	public SelectPlayerToStealFromCommand(ArrayList<String> listOfStealable) {
		this.victims = listOfStealable;
		fish = false;
	}


	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setSelectionReason(SelectionReason.STEAL_RESOURCE);
		if(fish){
			victims = gsm.getParticipants();
			victims.remove(gsm.getCurrentPlayer());
		}
		gsm.setPlayersToShow(victims);
		gsm.setShowSelectPlayerMenu(true);
	}

}
