package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;

import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.TerrainType;

public class HexagonImage extends MinuetoImage {

	public static final int SIZE = 125;
	private static int BORDERS = 25; // default number of pixels for the border
	private final static int HEXSIZE = 50;
	/**
	 * if this is true it means that the coordinate (0,0) are coordinates of the
	 * first vertex. if it is false it means that the coordinate are coordinates
	 * of the top left rectangle.
	 */
	public static boolean notAlternateVertex = true;

	private static int s = 0; // length of side
	private static int t = 0; // short side of the 30 degree triangle
	private static int r = 0; // radius-center to middle of each side
	private MinuetoColor color;

	public HexagonImage(Hexagon hex) {
		super(SIZE, SIZE);
		HexagonImage.setSide(HEXSIZE);
		drawPolygon(getColorByTerrainType(hex.getType()), drawCoordinates(0, 0));

		if (hex.getNumber() > 0) {
			draw(new MinuetoText("" + hex.getNumber(), new MinuetoFont("arial", 20, false, false), MinuetoColor.BLACK),
					HEXSIZE + 10, r);
		}
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
	}

	public int getHexSize() {
		return HEXSIZE;
	}

	public static void setBorders(int b) {
		BORDERS = b;
	}

	public int[] drawCoordinates(int x0, int y0) {
		int x = x0 + BORDERS;
		int y = y0 + BORDERS;

		int[] coordinates = new int[] { x, y, x + r, y - t, x + r + r, y, x + r + r, y + s, x + r, y + s + t, x,
				y + s };

		return coordinates;
	}

	private MinuetoColor getColorByTerrainType(TerrainType type) {
		switch (type) {
		case SEA:
			return new MinuetoColor(170, 204, 255);
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
