package catan.settlers.client.view.game.actions;


public interface Action{
	public boolean isPossible();
	public String getDescription();
	public String getTitle();
}
