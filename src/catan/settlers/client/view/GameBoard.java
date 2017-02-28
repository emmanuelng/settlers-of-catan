package catan.settlers.client.view;

import org.minueto.window.MinuetoFrame;

public class GameBoard {
	private MinuetoFrame gameFrame;
	
	public GameBoard(){
		gameFrame = GameFrame.getInstance();
		gameFrame.setVisible(true);
	}
}
