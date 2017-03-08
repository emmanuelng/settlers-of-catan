package catan.settlers.server.model.map;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.server.model.map.Hexagon.Direction;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.view.Intersection;

public class GameBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Hexagon hexagons[][];
	private ArrayList<Edge> edges;

	private int height = 3; // TODO: Make this value customizable
	private int length = 3; // TODO: Make this value customizable

	public GameBoard() {
		this.hexagons = new Hexagon[length][height];
		this.edges = new ArrayList<>();
		generateBoard();
	}

	/**
	 * Fills the 2D array with hexagons
	 */
	private void generateBoard() {
		// TODO: Would normally generate a random board
		hexagons[0][0] = new Hexagon(TerrainType.SEA, 4);
		hexagons[0][1] = new Hexagon(TerrainType.DESERT, 6);
		hexagons[0][2] = new Hexagon(TerrainType.PASTURE, 1);
		// addHexAt(new Hexagon(TerrainType.FOREST, 6), 1, 0);
		// addHexAt(new Hexagon(TerrainType.MOUNTAIN, 2), 1, 1);
		// addHexAt(new Hexagon(TerrainType.HILLS, 4), 1, 2);
		// addHexAt(new Hexagon(TerrainType.FIELD, 3), 2, 0);
		// addHexAt(new Hexagon(TerrainType.GOLDMINE, 2), 2, 1);
		// addHexAt(null, 2, 2); // Invisible hex

		populateAllEdges();
		System.out.println(edges.size());
	}

	private void populateAllEdges() {
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < height; y++) {
				populateEdgesHex(getHexagonAt(x, y));
			}
		}
	}

	private void populateEdgesHex(Hexagon hex) {
		System.out.println(hex);
		if (hex != null) {
			for (Direction dir : Direction.values()) {
				if (hex.getEdge(dir) == null) {
					Direction oppDir = Hexagon.getOppositeDir(dir);
					Hexagon neighbor = getHexNeighborInDir(hex, dir);
					System.out.println(dir + ": " + neighbor);
					if (neighbor != null) {
						if (neighbor.getEdge(oppDir) != null) {
							hex.setEdge(neighbor.getEdge(oppDir), dir);
							continue;
						}
					} else {
						Edge edge = new Edge();
						hex.setEdge(edge, dir);
						edges.add(edge);
					}
				}
			}
		}
		System.out.println("");
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
		// TODO: implement this
		return null;
	}

	public Intersection getIntersection(Hexagon hex1, Hexagon hex2, Hexagon hex3) {
		// TODO: implement this
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

	public int getLength() {
		return length;
	}

	public int getHeight() {
		return height;
	}
}
