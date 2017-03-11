package catan.settlers.client.view.game.handlers;

import java.util.ArrayList;
import java.util.HashSet;

import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;

public class BoardKeyboardHandler implements MinuetoKeyboardHandler{

	
	public BoardKeyboardHandler(){
	}
	@Override
	public void handleKeyPress(int value) {
		switch(value) {
		
		case MinuetoKeyboard.KEY_ENTER:
			if(ClientModel.instance.getCurrentIntersection() != null){
				System.out.println("Perfrom command instantaneously");
				ClientWindow.getInstance().getGameWindow().setDialogBox(null);
				ClientModel.instance.setCurrentIntersection(null);
			}
			break;
		
	}

	}

	@Override
	public void handleKeyRelease(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleKeyType(char arg0) {
		// TODO Auto-generated method stub
		
	}

}
