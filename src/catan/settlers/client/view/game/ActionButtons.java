package catan.settlers.client.view.game;

import org.minueto.image.MinuetoImage;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.actions.Action;
import catan.settlers.client.view.game.handlers.Clickable;

public class ActionButtons implements Clickable{

	private Action action;
	private int relativeX, relativeY, buttSize_X, buttSize_Y;
	
	public ActionButtons(Action action, int relativeX, int relativeY, int buttSize_X, int buttSize_Y){
		this.action=action;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.buttSize_X = buttSize_X;
		this.buttSize_Y = buttSize_Y;
		ClientWindow.getInstance().getGameWindow().getMouseHandler().register(this);
	}
	
	public boolean isClicked(int x, int y) {
		// TODO Auto-generated method stub
		return x > relativeX && x < relativeX + buttSize_X && y > relativeY +100 && y < relativeY + buttSize_Y+100;
	}

	@Override
	public void onclick() {
		action.sendCommand();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Button" + action;
	}

}
