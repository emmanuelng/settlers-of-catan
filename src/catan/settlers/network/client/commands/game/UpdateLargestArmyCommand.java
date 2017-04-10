package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class UpdateLargestArmyCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2937623524124463573L;
	private String player;

	public UpdateLargestArmyCommand(String player){
		this.player = player;
	}
	
	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setLargestArmy(player);

	}

}