package catan.settlers.client.view.game.handlers;

public interface InteractiveElement {

	public boolean isInteracted(int x, int y);

	/**
	 * This method should rename a UNIQUE string. Two interactive elements
	 * cannot return the same string.
	 */
	public String getName();

}