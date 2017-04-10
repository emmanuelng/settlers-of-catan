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
	private int p,t,s,vp;
	
	public UpdatePlayerCommand(int p, int t, int s, int vp){
		this.p=p;
		this.t=t;
		this.s=s;
		this.vp=vp;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		//System.out.println("P: "+player.getPoliticsLevel() + " T: "+ player.getTradeLevel() + " S: "+ player.getScienceLevel());
		gsm.setPoliticsImprovementLevel(p);
		gsm.setTradeImprovementLevel(t);
		gsm.setScienceImprovementLevel(s);
		gsm.setVictoryPoints(vp);
	}

}
