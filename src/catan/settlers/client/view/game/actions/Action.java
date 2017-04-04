package catan.settlers.client.view.game.actions;

public interface Action {

	/**
	 * Returns true if and only if the player is allowed to perform the action
	 */
	public boolean isPossible();

	/**
	 * Get the text that is going to be written on the button
	 */
	public String getDescription();

	/**
	 * Perform the action
	 */
	public void perform();
}
