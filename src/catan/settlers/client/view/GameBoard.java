package catan.settlers.client.view;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.MinuetoFileException;
import org.minueto.handlers.MinuetoWindowHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoText;

import org.minueto.window.MinuetoWindow;

import catan.settlers.client.model.HexagonImage;
import catan.settlers.client.model.HexagonImage.HexType;
import catan.settlers.client.model.IntersectionImage;

public class GameBoard implements MinuetoWindowHandler{
	
	private MinuetoWindow window;
	private MinuetoImage board;

	private MinuetoEventQueue eventQueue;
	private MinuetoFont fontArial14;
	private MinuetoImage helloWorld;
	private int BOARDSIZE = 19;
	
	// Images of text
	private BoardSurface boardSurface;
	//private MinuetoImage intersection[] = new MinuetoImage[20];
	
	// Should be keep the window open
	private boolean open;
	
	public GameBoard(){
		this.window = GameFrame.getInstance();
	}
	
	private void initialize() {
		// Build the event queue.
		eventQueue = new MinuetoEventQueue();
		
		// Register the window handler with the event queue.
		this.window.registerWindowHandler(this, eventQueue);		
		
		// Build the images of the text
		fontArial14 = new MinuetoFont("Arial",14,false, false);
		helloWorld = new MinuetoText("HelloWorld",fontArial14,MinuetoColor.BLUE);
		boardSurface = new BoardSurface(640,480);

		
		this.window.setVisible(true);
		this.window.setTitle("Cattlers of Seten");
	}
	
	public void start() {		
		initialize();
		
		// Keep window open
		open = true;
		
		boardSurface.clear(MinuetoColor.WHITE);
		//boardSurface.drawIntersection(100,100);
		boardSurface.drawHex(0, 0, HexType.BRICK);
		
		
		// Game/rendering loop
		while(open) {
		
			
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
		this.open=false;
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
