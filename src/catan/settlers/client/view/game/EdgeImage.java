package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoRectangle;

import catan.settlers.server.model.map.Edge;

public class EdgeImage extends MinuetoRectangle {

	public EdgeImage(Edge edge) {
		super(40, 10, new MinuetoColor(230, 230, 230), true);
	}

}
