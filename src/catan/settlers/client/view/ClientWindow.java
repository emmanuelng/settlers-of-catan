package catan.settlers.client.view;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.game.GameWindow;
import catan.settlers.client.view.setup.SetupWindow;
import catan.settlers.network.server.commands.game.GetListOfPlayersCommand;

public class ClientWindow {

	private static ClientWindow instance;

	private SetupWindow setupWindow;
	private GameWindow gameWindow;

	public static ClientWindow getInstance() {
		if (instance == null)
			instance = new ClientWindow();
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
		
		System.out.println(ClientModel.instance.getCurGameId());
		ClientModel.instance.getNetworkManager().sendCommand(new GetListOfPlayersCommand(ClientModel.instance.getCurGameId()));
		Thread workerThread = new Thread(new Worker(gameWindow));
		workerThread.start();
	}
}

class Worker implements Runnable {
	private GameWindow gamewindow;

	public Worker(GameWindow gameWindow) {
		this.gamewindow = gameWindow;
	}

	public void run() {
		gamewindow.start();
	}
}
