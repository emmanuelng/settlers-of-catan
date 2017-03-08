package catan.settlers.client.view.game;

public interface Clickable {

	public boolean isClicked(int x, int y);

	public void onclick();

	public String getName();

}
