package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;

import catan.settlers.server.model.map.Edge;

public class EdgeImage extends MinuetoRectangle implements Clickable {

	private int relativeX;
	private int relativeY;
	
	private int rot_width;
	private int rot_height;

	public EdgeImage(Edge edge, double rotation, int relativeX, int relativeY) {
		super(40, 10, new MinuetoColor(230, 230, 230), true);
		
		MinuetoImage rotated = rotate(rotation);
		this.rot_width = rotated.getWidth();
		this.rot_height = rotated.getHeight();		

		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	@Override
	public boolean isClicked(int x, int y) {
		return x > relativeX && x < relativeX + getWidth() && y > relativeY + 100 && y < relativeY + 100 + getHeight();
	}

	@Override
	public void onclick() {
		System.out.println("Edge was clicked!");
	}

	@Override
	public String getName() {
		return "Edge";
	}
	
	@Override
	public int getWidth() {
		return rot_width;
	}
	
	@Override
	public int getHeight() {
		return rot_height;
	}

}
