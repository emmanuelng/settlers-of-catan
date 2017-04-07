package catan.settlers.network.client.commands.game.cards;

import java.util.ArrayList;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class MasterMerchantCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -8234209994148244572L;
	private ArrayList<String> playersWithMoreVPs;
	private String currentPlayer;

	public MasterMerchantCommand(ArrayList<String> playersWithMoreVPs, String currentPlayer) {
		this.playersWithMoreVPs = playersWithMoreVPs;
		this.currentPlayer = currentPlayer;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setShowProgressCardMenu(false);

		if (currentPlayer.equals(ClientModel.instance.getUsername())) {
			gsm.setPlayersToShow(playersWithMoreVPs);
			gsm.setShowSelectPlayerMenu(true);
		} else {
			gsm.setdBox("Waiting for " + currentPlayer,
					currentPlayer + " played the Master Merchant card. Please wait.");
		}
	}

}
