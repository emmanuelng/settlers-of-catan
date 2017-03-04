package catan.settlers.client.view.game.handlers;

import org.minueto.handlers.MinuetoWindowHandler;

public class BoardWindowHandler implements MinuetoWindowHandler {
	
	/**
	 * Prints out a message when the MinuetoWindow gets the focus.
	 **/
	public void handleGetFocus() {
	
		System.out.println("Window got focus.");
	}
	
	/**
	 * Prints out a message when the MinuetoWindow loses the focus.
	 **/
	public void handleLostFocus() {
	
		System.out.println("Window lost focus.");
	}
	
	/**
	 * Prints out a message when the user tries to quit the application (by
	 * pressing the X in the top right corner, pressing Alt-F4, etc).
	 **/
	public void handleQuitRequest() {
	
		System.out.println("Window got a quit request.");
	}
	
	/**
	 * Prints out a message when the user minimizes the window.
	 **/
	public void handleMinimizeWindow() {
	
		System.out.println("Window was minimized.");
	}
	
	/** 
	 * Prints out a message when the user restores a window (from a minimize).
	 **/
	public void handleRestoreWindow() {
	
		System.out.println("Window was restore.");
	}
		
}	
