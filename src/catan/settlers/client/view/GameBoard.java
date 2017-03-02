package catan.settlers.client.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.handlers.MinuetoWindowHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoPanel;
import org.minueto.window.MinuetoWindow;

import catan.settlers.client.model.HexagonImage;
import catan.settlers.client.model.HexagonImage.HexType;
import catan.settlers.client.model.IntersectionImage;
import catan.settlers.client.view.setup.Login;

public class GameBoard implements MinuetoWindowHandler{
	
	private MinuetoWindow window;
	private MinuetoImage board;

	private MinuetoEventQueue eventQueue;
	private MinuetoFont font;
	
	// Images of text
	private MinuetoImage hex;
	private MinuetoImage intersection;
	
	// Should be keep the window open
	private boolean open;
	
	public GameBoard(){
		this.window = GameFrame.getInstance();
	}
	
	private void initialize() {
		// Build the event quue.
		eventQueue = new MinuetoEventQueue();
		
		// Register the window handler with the event queue.
		this.window.registerWindowHandler(this, eventQueue);		
		
		// Build the images of the text
		
		hex = new HexagonImage(0,0,HexType.BRICK);
		intersection = new IntersectionImage();
		
		this.window.setTitle("Cattlers of Seten");
	}
	
	public void start() {		
		initialize();
		
		// Keep window open
		open = true;
		MinuetoImage helloWorld = new MinuetoText("HelloWorld",fontArial14,Minueto.Color.BLUE);
		// Game/rendering loop
		while(open) {
		
			int n = 5;
			// Clear the window.
			this.window.clear(MinuetoColor.BLACK);
			
			// Draw the text on the screen.
			this.window.draw(helloWorld, 0, 0);
			//this.window.draw(hex, 0, 0);
			//this.window.draw(intersection, 100, 100);
			
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
