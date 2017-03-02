package catan.settlers.client.view;

import org.minueto.window.MinuetoFrame;

public class GameFrame extends MinuetoFrame{
	private static GameFrame instance = null;
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 500;
	
	private GameFrame(){
		super(WINDOW_WIDTH, WINDOW_HEIGHT, true);
	}
	
	public static GameFrame getInstance(){
		if (instance == null) {
			instance = new GameFrame();
		}
	return instance;
	}

}
