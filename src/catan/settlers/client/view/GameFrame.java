package catan.settlers.client.view;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	private static GameFrame instance = null;
	
	private GameFrame(){
		super.setTitle("Cattlers of Seten");
		super.setVisible(true);
		super.setSize(1920,1080);
	}
	
	public static GameFrame getInstance(){
		if (instance == null) {
			instance = new GameFrame();
		}
	return instance;
	}

}
