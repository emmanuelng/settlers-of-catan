package catan.settlers.client.view.game.handlers;

public interface Clickable {

	public boolean isClicked(int x, int y);

	public void onclick();

	/**
	 * This method should rename a UNIQUE string. Two clickable elements cannot
	 * return the same string.
	 */
	public String getName();

}
