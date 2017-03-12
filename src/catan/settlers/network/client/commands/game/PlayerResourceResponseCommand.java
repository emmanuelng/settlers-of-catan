package catan.settlers.network.client.commands.game;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.Player.ResourceType;

public class PlayerResourceResponseCommand implements ServerToClientCommand {

	int g,l,o,w,b,c,p,co;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerResourceResponseCommand(int g, int l, int o, int w, int b, int c, int p, int co){
		this.g=g;
		this.l=l;
		this.o=o;
		this.w=w;
		this.b=b;
		this.c=c;
		this.p=p;
		this.co=co;
	}
	
	@Override
	public void execute() {
		ClientWindow.getInstance().getGameWindow().getResourceBar().updateResources(ResourceType.GRAIN, g);
		ClientWindow.getInstance().getGameWindow().getResourceBar().updateResources(ResourceType.LUMBER, l);
		ClientWindow.getInstance().getGameWindow().getResourceBar().updateResources(ResourceType.ORE, o);
		ClientWindow.getInstance().getGameWindow().getResourceBar().updateResources(ResourceType.WOOL, w);
		ClientWindow.getInstance().getGameWindow().getResourceBar().updateResources(ResourceType.BRICK, b);
		ClientWindow.getInstance().getGameWindow().getResourceBar().updateResources(ResourceType.CLOTH, c);
		ClientWindow.getInstance().getGameWindow().getResourceBar().updateResources(ResourceType.PAPER, p);
		ClientWindow.getInstance().getGameWindow().getResourceBar().updateResources(ResourceType.COIN, co);
	}

}
