package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.game.HexagonImage.HexType;
import catan.settlers.client.view.game.handlers.BoardMouseHandler;
import catan.settlers.client.view.game.handlers.BoardWindowHandler;

public class GameWindow extends MinuetoFrame {

	public GameWindow() {
		super(ClientModel.WINDOW_WIDTH, ClientModel.WINDOW_HEIGHT, true);
	}
	
	private MinuetoEventQueue eventQueue;
	private MinuetoFont fontArial14;
	private MinuetoImage typeWords;
	private BoardSurface boardSurface;
	private boolean open;

	private void initialize() {
		// Build the event queue.
		eventQueue = new MinuetoEventQueue();

		// Register the window handler with the event queue.
		this.registerWindowHandler(new BoardWindowHandler(), eventQueue);
		this.registerMouseHandler(new BoardMouseHandler(), eventQueue);
		
		// Build the images of the text
		fontArial14 = new MinuetoFont("Arial", 14, false, false);
		typeWords = new MinuetoText("You can type what ever you want with this", fontArial14, MinuetoColor.BLUE);
		boardSurface = new BoardSurface(640, 480);

		this.setTitle("Cattlers of Seten");
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
			this.draw(boardSurface, 0, 0);

			// Handle all the events in the event queue.
			while (eventQueue.hasNext()) {
				eventQueue.handle();
			}

			// Render all graphics in the back buffer.
			this.render();
			Thread.yield();
		}

		// Close our window
		this.close();
	}
}
