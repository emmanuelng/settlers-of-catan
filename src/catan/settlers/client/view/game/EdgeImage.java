package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoRectangle;

import catan.settlers.server.model.map.Edge;

public class EdgeImage extends MinuetoRectangle implements Clickable {

	private int relativeX;
	private int relativeY;

	public EdgeImage(Edge edge, int relativeX, int relativeY) {
		super(40, 10, new MinuetoColor(230, 230, 230), true);

		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	@Override
	public boolean isClicked(int x, int y) {
		return x < relativeX + getWidth() && y < relativeY + getHeight() + 100;
	}

	@Override
	public void onclick() {
		System.out.println("Edge was clicked!");
	}

	@Override
	public String getName() {
		return "Edge";
	}

}
