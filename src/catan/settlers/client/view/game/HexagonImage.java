package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;

import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.TerrainType;

public class HexagonImage extends MinuetoImage {

	private static final HashMap<Hexagon, HexagonImage> hexagons = new HashMap<>();

	public static HexagonImage getHexagonImage(Hexagon hex) {
		if (hexagons.get(hex) == null) {
			hexagons.put(hex, new HexagonImage(hex));
		}
		return hexagons.get(hex);
	}

	public static final int HEIGHT = 100;
	public static final int WIDTH = (int) (0.8660254037844 * HEIGHT);
	public final static int HEXSIDE = 50;
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

	private HexagonImage(Hexagon hex) {
		super(WIDTH, HEIGHT);

		HexagonImage.setSide(HEXSIDE);
		drawPolygon(getColorByTerrainType(hex.getType()), drawCoordinates(0, 0));

		if (hex.getNumber() > 0) {
			MinuetoFont font = new MinuetoFont("arial", 20, true, false);
			MinuetoColor color = MinuetoColor.BLACK;
			MinuetoText text = new MinuetoText("" + hex.getNumber(), font, color, true);
			int posX = HexagonImage.WIDTH / 2 - text.getWidth() / 2;
			int posY = HexagonImage.HEIGHT / 2 - text.getHeight() / 2;

			draw(text, posX, posY);
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
		return HEXSIDE;
	}

	public static void setBorders(int b) {
	}

	public int[] drawCoordinates(int x0, int y0) {
		int x = x0;
		int y = (int) (y0 + (0.25 * HEIGHT));

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
