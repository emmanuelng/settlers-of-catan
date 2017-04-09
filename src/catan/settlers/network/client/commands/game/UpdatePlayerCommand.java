package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Player;

public class UpdatePlayerCommand implements ServerToClientCommand{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5407822065630018780L;
	private Player player;
	
	public UpdatePlayerCommand(Player p){
		this.player = p;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setPoliticsImprovementLevel(player.getPoliticsLevel());
		gsm.setTradeImprovementLevel(player.getTradeLevel());
		gsm.setScienceImprovementLevel(player.getScienceLevel());
		gsm.setVictoryPoints(player.getVP());
	}

}
