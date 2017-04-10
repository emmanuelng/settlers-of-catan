package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class FishResourceCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 6712965898194820822L;

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setSelectResourceMessage("Choose the resource you would like");
		gsm.setShowSelectResourceMenu(true);
		gsm.setShowSelectResourceMenuReason("Traded Fish");
	}
}
