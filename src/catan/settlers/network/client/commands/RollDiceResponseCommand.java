package catan.settlers.network.client.commands;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.DialogBox;

public class RollDiceResponseCommand implements ServerToClientCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int redValue, yellowValue, totalValue;
	
	public RollDiceResponseCommand(int redDieValue, int yellowDieValue){
		redValue=redDieValue;
		yellowValue=yellowDieValue;
		totalValue=redValue+yellowValue;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		ClientWindow.getInstance().getGameWindow().setDialogBox(new DialogBox(0, 0, 150, 50, "You rolled a "+redValue+" on the red die and a "+ yellowValue+" on the yellow die"));
	}

}
