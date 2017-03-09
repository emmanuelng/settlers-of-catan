package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

public class IntersectionImage extends MinuetoImage implements Clickable {

	private int relativeX;
	private int relativeY;

	public IntersectionImage(int relativeX, int relativeY) {
		super(20, 20);

		this.relativeX = relativeX;
		this.relativeY = relativeY;

		drawCircle(new MinuetoColor(204, 204, 204), 0, 0, 20);
	}

	@Override
	public boolean isClicked(int x, int y) {
		return x > relativeX && x < relativeX + getWidth() && y > relativeY + 100 && y < relativeY + 100 + getHeight();
	}

	@Override
	public void onclick() {
		System.out.println("Intersection was clicked!");

	}

	@Override
	public String getName() {
		return "Intersection";
	}
}
