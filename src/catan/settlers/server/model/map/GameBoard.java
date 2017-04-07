package catan.settlers.server.model.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import catan.settlers.server.model.map.Hexagon.Direction;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.model.units.Port.PortKind;

public class GameBoard implements Serializable {

	private static final long serialVersionUID = 9178542028428304082L;
	private Hexagon hexagons[][];
	private ArrayList<Edge> edges;
	private ArrayList<Intersection> intersections;
	private Hexagon robberHex;
	private Hexagon merchantHex;

	private int height = 7; // TODO: Make this value customizable
	private int length = 7; // TODO: Make this value customizable

	private int edgeId = 0;
	private int IntersectionId = 0;

	public GameBoard() {
		this.hexagons = new Hexagon[length][height];
		this.edges = new ArrayList<>();
		this.intersections = new ArrayList<>();
		generateBoard();
	}

	/**
	 * Fills the 2D array with hexagons
	 */
	private void generateBoard() {
		for (int x = 0; x < length; x++) {
			if (x == 1) {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][2] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][3] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][4] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][5] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			} else if (x == 5) {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][2] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][3] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][4] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][5] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			} else if (x == 0) {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][2] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][3] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][4] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][5] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			} else if (x == length - 1) {
				hexagons[x][0] = null;
				hexagons[x][1] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][2] = null;
				hexagons[x][3] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][4] = null;
				hexagons[x][5] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][6] = null;

			} else {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][2] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][3] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][4] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][5] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			}
		}
		populateAllEdgesAndIntersections();
		randomizeHexes();
		stitchEdgesAndIntersections();
		setPortables();
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
					Edge edge = new Edge(edgeId);
					edgeId++;
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

					Intersection i = new Intersection(IntersectionId);
					IntersectionId++;
					hex.setIntersection(i, loc);
					intersections.add(i);
				}
			}
		}
	}

	private void randomizeHexes() {
		ArrayList<TerrainType> terrainPool = new ArrayList<>();
		terrainPool.add(TerrainType.HILLS);
		terrainPool.add(TerrainType.HILLS);
		terrainPool.add(TerrainType.HILLS);
		terrainPool.add(TerrainType.MOUNTAIN);
		terrainPool.add(TerrainType.MOUNTAIN);
		terrainPool.add(TerrainType.MOUNTAIN);
		terrainPool.add(TerrainType.PASTURE);
		terrainPool.add(TerrainType.PASTURE);
		terrainPool.add(TerrainType.PASTURE);
		terrainPool.add(TerrainType.PASTURE);
		terrainPool.add(TerrainType.FIELD);
		terrainPool.add(TerrainType.FIELD);
		terrainPool.add(TerrainType.FIELD);
		terrainPool.add(TerrainType.FIELD);
		terrainPool.add(TerrainType.FOREST);
		terrainPool.add(TerrainType.FOREST);
		terrainPool.add(TerrainType.FOREST);
		terrainPool.add(TerrainType.FOREST);
		terrainPool.add(TerrainType.DESERT);
		Collections.shuffle(terrainPool);

		ArrayList<Integer> diceValues = new ArrayList<>();
		diceValues.addAll(Arrays.asList(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
		Collections.shuffle(diceValues);

		for (int x = 0; x < length; x++) {
			for (int y = 0; y < height; y++) {
				if (hexagons[x][y] != null) {
					if (hexagons[x][y].getType() != TerrainType.SEA) {
						TerrainType t = terrainPool.remove(0);
						if (t == TerrainType.DESERT) {
							hexagons[x][y].setType(t);
							hexagons[x][y].setNumber(0);
							robberHex = hexagons[x][y];
						} else {
							hexagons[x][y].setType(t);
							hexagons[x][y].setNumber(diceValues.remove(0));
						}
					}
				}
			}
		}
	}

	private void stitchEdgesAndIntersections() {
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < height; y++) {
				if (hexagons[x][y] != null) {
					Hexagon h = hexagons[x][y];
					h.getIntersection(IntersectionLoc.TOP).addEdge(h.getEdge(Direction.NORTHWEST));
					h.getIntersection(IntersectionLoc.TOP).addEdge(h.getEdge(Direction.NORTHEAST));
					h.getIntersection(IntersectionLoc.TOP).addHex(h);

					h.getIntersection(IntersectionLoc.TOPRIGHT).addEdge(h.getEdge(Direction.NORTHWEST));
					h.getIntersection(IntersectionLoc.TOPRIGHT).addEdge(h.getEdge(Direction.EAST));
					h.getIntersection(IntersectionLoc.TOPRIGHT).addHex(h);

					h.getIntersection(IntersectionLoc.BOTTOMRIGHT).addEdge(h.getEdge(Direction.EAST));
					h.getIntersection(IntersectionLoc.BOTTOMRIGHT).addEdge(h.getEdge(Direction.SOUTHEAST));
					h.getIntersection(IntersectionLoc.BOTTOMRIGHT).addHex(h);

					h.getIntersection(IntersectionLoc.BOTTOM).addEdge(h.getEdge(Direction.SOUTHEAST));
					h.getIntersection(IntersectionLoc.BOTTOM).addEdge(h.getEdge(Direction.SOUTHWEST));
					h.getIntersection(IntersectionLoc.BOTTOM).addHex(h);

					h.getIntersection(IntersectionLoc.BOTTOMLEFT).addEdge(h.getEdge(Direction.SOUTHWEST));
					h.getIntersection(IntersectionLoc.BOTTOMLEFT).addEdge(h.getEdge(Direction.WEST));
					h.getIntersection(IntersectionLoc.BOTTOMLEFT).addHex(h);

					h.getIntersection(IntersectionLoc.TOPLEFT).addEdge(h.getEdge(Direction.WEST));
					h.getIntersection(IntersectionLoc.TOPLEFT).addEdge(h.getEdge(Direction.NORTHWEST));
					h.getIntersection(IntersectionLoc.TOPLEFT).addHex(h);

					h.getEdge(Direction.NORTHEAST).addLeftEdge(h.getEdge(Direction.NORTHWEST));
					h.getEdge(Direction.NORTHEAST).addRightEdge(h.getEdge(Direction.EAST));
					h.getEdge(Direction.NORTHEAST).setIntersections(h.getIntersection(IntersectionLoc.TOP),
							h.getIntersection(IntersectionLoc.TOPRIGHT));

					h.getEdge(Direction.EAST).addLeftEdge(h.getEdge(Direction.NORTHEAST));
					h.getEdge(Direction.EAST).addRightEdge(h.getEdge(Direction.SOUTHEAST));
					h.getEdge(Direction.EAST).setIntersections(h.getIntersection(IntersectionLoc.TOPRIGHT),
							h.getIntersection(IntersectionLoc.BOTTOMRIGHT));

					h.getEdge(Direction.SOUTHEAST).addLeftEdge(h.getEdge(Direction.EAST));
					h.getEdge(Direction.SOUTHEAST).addRightEdge(h.getEdge(Direction.SOUTHWEST));
					h.getEdge(Direction.SOUTHEAST).setIntersections(h.getIntersection(IntersectionLoc.BOTTOMRIGHT),
							h.getIntersection(IntersectionLoc.BOTTOM));

					h.getEdge(Direction.SOUTHWEST).addRightEdge(h.getEdge(Direction.SOUTHEAST));
					h.getEdge(Direction.SOUTHWEST).addLeftEdge(h.getEdge(Direction.WEST));
					h.getEdge(Direction.SOUTHWEST).setIntersections(h.getIntersection(IntersectionLoc.BOTTOM),
							h.getIntersection(IntersectionLoc.BOTTOMLEFT));

					h.getEdge(Direction.WEST).addRightEdge(h.getEdge(Direction.SOUTHWEST));
					h.getEdge(Direction.WEST).addLeftEdge(h.getEdge(Direction.NORTHWEST));
					h.getEdge(Direction.WEST).setIntersections(h.getIntersection(IntersectionLoc.BOTTOMLEFT),
							h.getIntersection(IntersectionLoc.TOPLEFT));

					h.getEdge(Direction.NORTHWEST).addRightEdge(h.getEdge(Direction.WEST));
					h.getEdge(Direction.NORTHWEST).addLeftEdge(h.getEdge(Direction.NORTHEAST));
					h.getEdge(Direction.NORTHWEST).setIntersections(h.getIntersection(IntersectionLoc.TOPLEFT),
							h.getIntersection(IntersectionLoc.TOP));
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

	public Intersection getIntersectionById(int id) {
		return intersections.get(id);
	}

	public Edge getEdgeById(int id) {
		return edges.get(id);
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

	public Hexagon getRobberHex() {
		return robberHex;
	}

	public void setRobberHex(Hexagon h) {
		robberHex = h;
	}

	public Hexagon getMerchantHex() {
		return merchantHex;
	}

	public void setMerchantHex(Hexagon h) {
		merchantHex = h;
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

	private void setPortables() {
		hexagons[2][1].getIntersection(IntersectionLoc.TOP).setPortable(PortKind.ALLPORT);
		hexagons[2][1].getIntersection(IntersectionLoc.TOPLEFT).setPortable(PortKind.ALLPORT);
		hexagons[1][2].getIntersection(IntersectionLoc.TOPLEFT).setPortable(PortKind.BRICKPORT);
		hexagons[1][2].getIntersection(IntersectionLoc.BOTTOMLEFT).setPortable(PortKind.BRICKPORT);
		hexagons[1][4].getIntersection(IntersectionLoc.TOPLEFT).setPortable(PortKind.LUMBERPORT);
		hexagons[1][4].getIntersection(IntersectionLoc.BOTTOMLEFT).setPortable(PortKind.LUMBERPORT);
		hexagons[2][5].getIntersection(IntersectionLoc.BOTTOMLEFT).setPortable(PortKind.ALLPORT);
		hexagons[2][5].getIntersection(IntersectionLoc.BOTTOM).setPortable(PortKind.ALLPORT);
		hexagons[3][5].getIntersection(IntersectionLoc.BOTTOM).setPortable(PortKind.OREPORT);
		hexagons[3][5].getIntersection(IntersectionLoc.BOTTOMRIGHT).setPortable(PortKind.OREPORT);
		hexagons[4][4].getIntersection(IntersectionLoc.BOTTOMRIGHT).setPortable(PortKind.WOOLPORT);
		hexagons[4][4].getIntersection(IntersectionLoc.BOTTOM).setPortable(PortKind.WOOLPORT);
		hexagons[5][3].getIntersection(IntersectionLoc.TOPRIGHT).setPortable(PortKind.ALLPORT);
		hexagons[5][3].getIntersection(IntersectionLoc.BOTTOMRIGHT).setPortable(PortKind.ALLPORT);
		hexagons[4][2].getIntersection(IntersectionLoc.TOP).setPortable(PortKind.GRAINPORT);
		hexagons[4][2].getIntersection(IntersectionLoc.TOPRIGHT).setPortable(PortKind.GRAINPORT);
		hexagons[3][1].getIntersection(IntersectionLoc.TOP).setPortable(PortKind.ALLPORT);
		hexagons[3][1].getIntersection(IntersectionLoc.TOPRIGHT).setPortable(PortKind.ALLPORT);
	}

	@Override
	public String toString() {
		String result = "";
		for (Intersection i : intersections) {
			if (i.getUnit() != null) {
				result += i.getUnit() + "\n";
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

	public ArrayList<Intersection> getIntersections() {
		return intersections;
	}

	public int[] getHex_coords(Hexagon hex) {
		int[] coords = new int[2];

		for (int x = 0; x < length; x++) {
			for (int y = 0; y < height; y++)
				if (hexagons[x][y] == hex) {
					coords[0] = x;
					coords[1] = y;
					return coords;
				}
		}

		return null;
	}
}