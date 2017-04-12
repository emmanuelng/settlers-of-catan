package catan.settlers.network.client.commands.game.fish;

import java.util.ArrayList;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class FishStealResourceCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -6979280825224247284L;
	private String playerWhoPlayedCard;
	private ArrayList<String> choosablePlayers;

	public FishStealResourceCommand(String username, ArrayList<String> choosablePlayers) {
		this.playerWhoPlayedCard = username;
		this.choosablePlayers = choosablePlayers;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowFishMenu(false);
		
		if (playerWhoPlayedCard.equals(ClientModel.instance.getUsername())) {
			gsm.setPlayersToShow(choosablePlayers);
			gsm.setShowSelectPlayerMenu(true);
		} else {
			gsm.setdBox(playerWhoPlayedCard + " is stealing resources", "Pleas wait.");
		}

	}

}
