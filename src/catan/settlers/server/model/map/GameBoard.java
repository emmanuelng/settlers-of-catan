package catan.settlers.server.model.map;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.server.model.map.Hexagon.Direction;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Hexagon.TerrainType;

public class GameBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Hexagon hexagons[][];
	private ArrayList<Edge> edges;
	private ArrayList<Intersection> instersections;
	private int height = 3; // TODO: Make this value customizable
	private int length = 3; // TODO: Make this value customizable

	public GameBoard() {
		this.hexagons = new Hexagon[length][height];
		this.edges = new ArrayList<>();
		this.instersections = new ArrayList<>();
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
		hexagons[1][0] = new Hexagon(TerrainType.FOREST, 6);
		hexagons[1][1] = new Hexagon(TerrainType.MOUNTAIN, 2);
		hexagons[1][2] = new Hexagon(TerrainType.HILLS, 4);
		hexagons[2][0] = new Hexagon(TerrainType.FIELD, 3);
		hexagons[2][1] = new Hexagon(TerrainType.GOLDMINE, 2);
		hexagons[2][2] = null; // Invisible hex

		populateAllEdgesAndIntersections();
	}

	private void populateAllEdgesAndIntersections() {
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < height; y++) {
				populateEdgesHex(getHexagonAt(x, y));
				populateIntersectionsHex(getHexagonAt(x, y));
			}
		}
	}

	private void populateEdgesHex(Hexagon hex) {
		if (hex != null) {
			for (Direction dir : Direction.values()) {
				if (hex.getEdge(dir) == null) {
					Direction oppDir = Hexagon.getOppositeDir(dir);
					Hexagon neighbor = getHexNeighborInDir(hex, dir);
					if (neighbor != null) {
						if (neighbor.getEdge(oppDir) != null) {
							hex.setEdge(neighbor.getEdge(oppDir), dir);
							continue;
						}
					}

					Edge edge = new Edge();
					hex.setEdge(edge, dir);
					edges.add(edge);
				}
			}
		}
	}

	private void populateIntersectionsHex(Hexagon hex) {
		if (hex != null) {
			for (IntersectionLoc loc : IntersectionLoc.values()) {
				if (hex.getIntersection(loc) == null) {
					Direction adj[] = Hexagon.getAdjacentDirs(loc);

					Hexagon neighbor1 = getHexNeighborInDir(hex, adj[0]);
					Hexagon neighbor2 = getHexNeighborInDir(hex, adj[1]);

					if (neighbor1 != null) {
						IntersectionLoc opp = Hexagon.getOppositeIntersection(loc, adj[0]);
						Intersection i = neighbor1.getIntersection(opp);
						if (i != null) {
							hex.setIntersection(i, loc);
							continue;
						}
					}

					if (neighbor2 != null) {
						IntersectionLoc opp = Hexagon.getOppositeIntersection(loc, adj[1]);
						Intersection i = neighbor2.getIntersection(opp);
						if (i != null) {
							hex.setIntersection(i, loc);
							continue;
						}
					}

					Intersection i = new Intersection();
					hex.setIntersection(i, loc);
					instersections.add(i);
				}
			}
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