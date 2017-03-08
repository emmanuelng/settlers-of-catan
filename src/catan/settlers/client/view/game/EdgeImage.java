package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

public class EdgeImage extends MinuetoImage {
	private MinuetoImage edgeImage;
	private int[] roadCoordinates;

	public static enum EdgeType {
		EAST, WEST, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST
	};

	public EdgeImage(int startX, int startY, int endX, int endY, EdgeType etype) {
		edgeImage = new MinuetoImage(1920, 1080);
		switch (etype) {
		case EAST:
			roadCoordinates = new int[] { startX, startY + 4, startX, startY - 4, endX, endY - 4, endX, endY + 4 };
			break;
		case WEST:
			roadCoordinates = new int[] { startX, startY + 4, startX, startY - 4, endX, endY - 4, endX, endY + 4 };
			break;
		case NORTHWEST:
			roadCoordinates = new int[] { startX + 2, startY + 2, startX - 2, startY - 2, endX - 2, endY - 2, endX + 2,
					endY + 2 };
			break;
		case SOUTHEAST:
			roadCoordinates = new int[] { startX + 2, startY + 2, startX - 2, startY - 2, endX - 2, endY - 2, endX + 2,
					endY + 2 };
			break;
		case NORTHEAST:
			roadCoordinates = new int[] { startX - 2, startY + 2, startX + 2, startY - 2, endX + 2, endY - 2, endX - 2,
					endY + 2 };
			break;
		case SOUTHWEST:
			roadCoordinates = new int[] { startX - 2, startY + 2, startX + 2, startY - 2, endX + 2, endY - 2, endX - 2,
					endY + 2 };
			break;
		}
		for (int i : roadCoordinates) {
			System.out.println(i);
		}
		edgeImage.drawPolygon(MinuetoColor.BLACK, roadCoordinates);
	}

	public MinuetoImage getEdgeImage() {
		return edgeImage;
	}
}
