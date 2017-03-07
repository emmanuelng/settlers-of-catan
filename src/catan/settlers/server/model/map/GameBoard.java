package catan.settlers.server.model.map;

import java.io.Serializable;

import catan.settlers.server.model.map.Hexagon.Direction;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.view.Intersection;

public class GameBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Hexagon hexagons[][];

	private int height = 3; // TODO: Make this value customizable
	private int length = 3; // TODO: Make this value customizable

	public GameBoard() {
		this.hexagons = new Hexagon[length][height];
		generateBoard();
	}

	/**
	 * Fills the 2D array with hexagons
	 */
	private void generateBoard() {
		// TODO: Would normally generate a random board
		addHexAt(new Hexagon(TerrainType.SEA, 4), 0, 0);
		addHexAt(new Hexagon(TerrainType.DESERT, 6), 0, 1);
		addHexAt(new Hexagon(TerrainType.PASTURE, 1), 0, 2);
		addHexAt(new Hexagon(TerrainType.FOREST, 6), 1, 0);
		addHexAt(new Hexagon(TerrainType.MOUNTAIN, 2), 1, 1);
		addHexAt(new Hexagon(TerrainType.HILLS, 4), 1, 2);
		addHexAt(new Hexagon(TerrainType.FIELD, 3), 2, 0);
		addHexAt(new Hexagon(TerrainType.GOLDMINE, 2), 2, 1);
		addHexAt(null, 2, 2); // Invisible hex

	}

	/**
	 * Adds hexagons in the 2D array and populates edges/intersections
	 */
	private void addHexAt(Hexagon hex, int x, int y) {
		if (isValidCoords(new Coordinates(x, y))) {
			hexagons[x][y] = hex;
		}
	}

	public Hexagon getHexagonAt(int x, int y) {
		/*
		 * using offset coordinates ("even-r" horizontal layout)
		 * (http://www.redblobgames.com/grids/hexagons/#coordinates)
		 */

		if (x >= length || y >= height) {
			return null;
		} else {
			return hexagons[x][y];
		}
	}

	/**
	 * Returns the neighbor given an hexagon and a direction
	 * 
	 * @return The neighbor or null if the hex does not have a neighbor in the
	 *         given direction
	 */
	public Hexagon getHexNeighborInDir(Hexagon curHex, Direction direction) {
		Coordinates curHex_coords = getCoords(curHex);

		if (curHex != null) {
			Coordinates wantedHex_coords = curHex_coords.getCoordsInDir(direction);
			if (isValidCoords(wantedHex_coords)) {
				// The hex has a neighbor in this direction
				return hexagons[wantedHex_coords.getX()][wantedHex_coords.getY()];
			}
		}

		return null;
	}

	public Edge getEdge(Hexagon hex1, Hexagon hex2) {
		return null;
	}

	public Intersection getIntersection(Hexagon hex1, Hexagon hex2, Hexagon hex3) {
		return null;
	}

	/**
	 * Private helpers; nothing important here
	 */

	private boolean isValidCoords(Coordinates curHex_coords) {
		int x = curHex_coords.getX();
		int y = curHex_coords.getY();
		return x >= 0 && y >= 0 && x < length && y < length;
	}

	private Coordinates getCoords(Hexagon curHex) {
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < height; y++) {
				if (hexagons[x][y] == curHex) {
					return new Coordinates(x, y);
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		String result = "";
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < height; y++) {
				result += "[" + x + "][" + y + "] = " + getHexagonAt(x, y) + "\n";
			}
		}
		return result;
	}
}
