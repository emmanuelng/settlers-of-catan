package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class UpdateFishCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 5928066060635183357L;
	private int numFish;

	public UpdateFishCommand(int numFish){
		this.numFish = numFish;
	}
	
	@Override
	public void execute() {
		ClientModel.instance.getGameStateManager().setNumFish(numFish);
	}

}
