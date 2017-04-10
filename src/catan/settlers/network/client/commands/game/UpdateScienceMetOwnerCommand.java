package catan.settlers.network.client.commands.game;

import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.game.FlipchartLayer.Field;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Player;

public class UpdateScienceMetOwnerCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -227597780281412527L;
	private String scienceMetOwner;

	public UpdateScienceMetOwnerCommand(String scienceMetOwner){
		this.scienceMetOwner = scienceMetOwner;
	}
	
	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setScienceMetOwner(scienceMetOwner);
	}

}
