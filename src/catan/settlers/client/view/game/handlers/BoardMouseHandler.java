package catan.settlers.client.view.game.handlers;

import org.minueto.handlers.MinuetoMouseHandler;

public class BoardMouseHandler implements MinuetoMouseHandler {
	
	/**
	 * Print out the mouse button values and mouse location when the user 
	 * presses a mouse button. Constants to identify mouse button values are 
	 * available in the MinuetoMouse class.
	 **/
	public void handleMousePress(int x, int y, int button) {
	
		System.out.println("Mouse click on button " + button + " detected at " + x + "," + y);	
	}
	
	/**
	 * Print out the mouse button values and mouse location when the user 
	 * releases a mouse button. Constants to identify mouse button values are 
	 * available in the MinuetoMouse class.
	 **/
	public void handleMouseRelease(int x, int y, int button) {
		
		System.out.println("Mouse release on button " + button + " detected at " + x + "," + y);
	}
	
	/**
	 * This method is called to handle mouse move events. We are not printing
	 * any message since a LOT of these events are generated when the mouse
	 * is moved.
	 **/
	public void handleMouseMove(int x, int y) {		
	
		// Not going to print on this event.
	}
}