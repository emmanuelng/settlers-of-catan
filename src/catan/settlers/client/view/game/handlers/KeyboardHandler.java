package catan.settlers.client.view.game.handlers;

import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;

public class KeyboardHandler implements MinuetoKeyboardHandler{

	
	public KeyboardHandler(){
	}
	@Override
	public void handleKeyPress(int value) {
		switch(value) {
		
		case MinuetoKeyboard.KEY_ENTER:
			if(ClientModel.instance.getGameStateManager().getSelectedIntersection() != null){
				System.out.println("Perfrom command instantaneously"); //send build settlement command
				ClientWindow.getInstance().getGameWindow().setDialogBox(null);
				ClientModel.instance.getGameStateManager().setSelectedIntersection(null);
			}
			if(ClientWindow.getInstance().getGameWindow().getTradeMenu().getSurface()!=null){
				System.out.println("Perfrom command instantaneously"); //send resource offer
				ClientWindow.getInstance().getGameWindow().getTradeMenu().setSurface(null);
				ClientWindow.getInstance().getGameWindow().getTradeMenu().confirmTradeOffer();
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
