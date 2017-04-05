package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;

public class MoveRobberCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -1185923573666264410L;
	private boolean isBishop;
	
	public MoveRobberCommand(boolean isBishop){
		this.isBishop = isBishop;
	}
	
	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setCanMoveRobber(true,isBishop);

		gsm.setdBox("Select a hexagon to block with the robber",
				"Also select a settlement/city to steal a random resource from.");
	}

}
