package catan.settlers.client.view;

import catan.settlers.client.view.game.GameWindow;
import catan.settlers.client.view.game.HexagonMap;
import catan.settlers.client.view.setup.SetupWindow;

public class ClientWindow {
	
	private static ClientWindow instance;
	
	private SetupWindow setupWindow;
	private GameWindow gameWindow;
	
	public static ClientWindow getInstance() {
		if (instance == null) instance = new ClientWindow();
		return instance;
	}
	
	private ClientWindow() {
		setupWindow = new SetupWindow();
		gameWindow = new GameWindow();
		
		setupWindow.setVisible(true);
		gameWindow.setVisible(false);
	}
	
	
	public SetupWindow getSetupWindow() {
		return setupWindow;
	}
	
	public GameWindow getGameWindow() {
		return gameWindow;
	}
	
	public void switchToSetup() {
		gameWindow.setVisible(false);
		setupWindow.setVisible(true);
	}
	
	public void switchToGame() {
		setupWindow.setVisible(false);
		gameWindow.setVisible(true);
		
		// Call drawing methods here
		
		// For testing purposes
		gameWindow.draw(new HexagonMap().DrawHexagonMap(0, 0), 0, 0);
		gameWindow.render();
	}
}
