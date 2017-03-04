package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.handlers.MinuetoWindowHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoWindow;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.HexagonImage.HexType;

public class GameBoard implements MinuetoWindowHandler {

	private MinuetoWindow window;
	private MinuetoEventQueue eventQueue;
	private MinuetoFont fontArial14;
	private MinuetoImage typeWords;
	private BoardSurface boardSurface;
	private boolean open;

	public GameBoard() {
		this.window = ClientWindow.getInstance().getGameWindow();
	}

	private void initialize() {
		// Build the event queue.
		eventQueue = new MinuetoEventQueue();

		// Register the window handler with the event queue.
		this.window.registerWindowHandler(this, eventQueue);

		// Build the images of the text
		fontArial14 = new MinuetoFont("Arial", 14, false, false);
		typeWords = new MinuetoText("You can type what ever you want with this", fontArial14, MinuetoColor.BLUE);
		boardSurface = new BoardSurface(640, 480);

		this.window.setVisible(true);
		this.window.setTitle("Cattlers of Seten");
	}

	public void start() {
		initialize();

		// Keep window open
		open = true;

		boardSurface.clear(MinuetoColor.WHITE);
		boardSurface.drawIntersection(100, 100);
		boardSurface.drawHex(0, 0, HexType.BRICK);
		boardSurface.draw(typeWords, 20, 20);

		// Game/rendering loop
		while (open) {

			// Draw the text on the screen.
			this.window.draw(boardSurface, 0, 0);

			// Handle all the events in the event queue.
			while (eventQueue.hasNext()) {
				eventQueue.handle();
			}

			// Render all graphics in the back buffer.
			this.window.render();
			Thread.yield();
		}

		// Close our window
		this.open = false;
	}

	@Override
	public void handleGetFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleLostFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMinimizeWindow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleQuitRequest() {
		// TODO Auto-generated method stub
		this.open = false;

	}

	@Override
	public void handleRestoreWindow() {
		// TODO Auto-generated method stub

	}

}
