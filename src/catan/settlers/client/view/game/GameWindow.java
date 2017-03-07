package catan.settlers.client.view.game;

import org.minueto.MinuetoEventQueue;
import org.minueto.window.MinuetoFrame;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.game.handlers.BoardMouseHandler;
import catan.settlers.client.view.game.handlers.BoardWindowHandler;
import catan.settlers.network.server.commands.game.GetGameBoardCommand;
import catan.settlers.server.model.map.GameBoard;

public class GameWindow extends MinuetoFrame {

	public GameWindow() {
		super(ClientModel.WINDOW_WIDTH, ClientModel.WINDOW_HEIGHT, true);
	}

	private MinuetoEventQueue eventQueue;
	private boolean open;
	private boolean waitingForGameBoard;

	public void start() {
		initialize();
		waitForGameBoard();

		// Keep window open
		open = true;

		// Event loop
		while (open) {
			while (eventQueue.hasNext()) {
				eventQueue.handle();
			}
		}
		
		// Close window
		close();
	}
	
	public void updateWindow(GameBoard board) {
		GameBoardImage gameBoard = new GameBoardImage(board);
		draw(gameBoard, 0, 0);
		render();
	}

	private void initialize() {
		// Window settings
		this.setTitle("Settlers of Catan");

		// Build the event queue.
		eventQueue = new MinuetoEventQueue();

		// Register the window handler with the event queue.
		this.registerWindowHandler(new BoardWindowHandler(), eventQueue);
		this.registerMouseHandler(new BoardMouseHandler(), eventQueue);
	}

	private void waitForGameBoard() {
		waitingForGameBoard = true;
		GetGameBoardCommand req = new GetGameBoardCommand(ClientModel.instance.getCurGameId());
		ClientModel.instance.getNetworkManager().sendCommand(req);

		while (waitingForGameBoard) {
			// Wait for the game board. We could display a loading screen here
		}
	}

	public void receiveGameBoard(GameBoard board) {
		waitingForGameBoard = false;
		updateWindow(board);
	}

}
