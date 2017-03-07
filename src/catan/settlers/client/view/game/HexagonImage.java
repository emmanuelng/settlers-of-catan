package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.TerrainType;

public class HexagonImage extends MinuetoImage {

	private static int BORDERS = 25; // default number of pixels for the border
	private final static int BSIZE = 19; // changes size of the board
	private final static int HEXSIZE = 50;
	public final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS * 3;

	/**
	 * if this is true it means that the coordinate (0,0) are coordinates of the
	 * first vertex. if it is false it means that the coordinate are coordinates
	 * of the top left rectangle.
	 */
	public static boolean notAlternateVertex = true;

	private static int s = 0; // length of side
	private static int t = 0; // short side of the 30 degree triangle
	private static int r = 0; // radius-center to middle of each side
	private static int h = 0; // distance between centers of two neighbor hexes

	private MinuetoColor color;

	public HexagonImage(Hexagon hex) {
		super(SCRSIZE, SCRSIZE);

		HexagonImage.setHeight(HEXSIZE);
		HexagonImage.setSide(HEXSIZE);

		drawPolygon(getColorByTerrainType(hex.getType()), drawCoordinates(0, 0));
	}

	public MinuetoColor getColor() {
		return color;
	}

	public void setColor(MinuetoColor color) {
		this.color = color;
	}

	public static void setSide(int side) {
		s = side;
		t = (int) (s / 2);
		r = (int) (s * 0.8660254037844);
		h = 2 * r;
	}

	public static void setHeight(int height) {
		// distance between two centers, or the size of the coordinate
		h = height;

		// radius of the inscribed circle
		r = h / 2;

		// short side is (h/2)/cos30 = (h/2)/(sqrt(3)/2) = h/sqrt(3)
		t = (int) (h / 1.73205);

		// side length is (h/2) tan30 = h/2/sqrt(3) = radius/sqrt(3)
		s = (int) (r / 1.73205);

	}

	public static void setBorders(int b) {
		BORDERS = b;
	}

	public int[] drawCoordinates(int x0, int y0) {
		int x = x0 + BORDERS;
		int y = y0 + BORDERS;

		int[] coordinates;

		if (notAlternateVertex) {
			coordinates = new int[] { x, y, x + s, y, x + s + t, y + r, x + s, y + r + r, x, y + r + r, x - t, y + r };
		} else {
			coordinates = new int[] { x + t, y, x + s + t, y, x + s + t + t, y + r, x + s + t, y + r + r, x + t,
					y + r + r, x, y + r };
		}

		return coordinates;
	}

	private MinuetoColor getColorByTerrainType(TerrainType type) {
		switch (type) {
		case SEA:
			return new MinuetoColor(128, 179, 255);
		case DESERT:
			return new MinuetoColor(222, 205, 135);
		case PASTURE:
			return new MinuetoColor(170, 222, 135);
		case FOREST:
			return new MinuetoColor(55, 200, 113);
		case MOUNTAIN:
			return new MinuetoColor(200, 190, 183);
		case HILLS:
			return new MinuetoColor(222, 170, 135);
		case FIELD:
			return new MinuetoColor(198, 233, 175);
		case GOLDMINE:
			return new MinuetoColor(229, 255, 128);
		default:
			return new MinuetoColor(236, 236, 236);
		}
	}
}
