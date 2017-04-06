package catan.settlers.network.client.commands.game;

import java.util.ArrayList;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class UpdateCardsCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 408107640196511943L;
	private ArrayList<ProgressCardType> progressCards;

	public UpdateCardsCommand(ArrayList<ProgressCardType> progressCards) {
		this.progressCards = progressCards;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setProgressCards(progressCards);
	}

}
