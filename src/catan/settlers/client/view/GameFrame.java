package catan.settlers.client.view;

import org.minueto.window.MinuetoFrame;

public class GameFrame extends MinuetoFrame{
	private static GameFrame instance = null;
	
	private GameFrame(){
		super(1920, 1080, true);
	}
	
	public static GameFrame getInstance(){
		if (instance == null) {
			instance = new GameFrame();
		}
	return instance;
	}
}
