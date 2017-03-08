package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

public class IntersectionImage extends MinuetoImage {

	public IntersectionImage() {
		super(20, 20);
		drawCircle(new MinuetoColor(204, 204, 204), 0, 0, 20);
	}
}
