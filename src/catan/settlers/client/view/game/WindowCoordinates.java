package catan.settlers.client.view.game;

public class WindowCoordinates {

	private int x;
	private int y;

	public WindowCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WindowCoordinates) {
			WindowCoordinates other = (WindowCoordinates) obj;
			return other.x == x && other.y == y;
		}
		return false;
	}
}
