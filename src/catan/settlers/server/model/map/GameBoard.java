package catan.settlers.server.model.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import catan.settlers.server.model.Player;
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
	private Player merchantOwner;
	private boolean bootDrawn;

	public GameBoard() {
		this.hexagons = new Hexagon[length][height];
		this.edges = new ArrayList<>();
		this.intersections = new ArrayList<>();
		this.bootDrawn = false;
		generateBoard();
	}

	/**
	 * Fills the 2D array with hexagons
	 */
	private void generateBoard() {
		for (int x = 0; x < length; x++) {
			if (x == 0) {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][2] = new Hexagon(TerrainType.FISHINGGROUND, 1);
				hexagons[x][3] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][4] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][5] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			} else if (x == 1) {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][2] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][3] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][4] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][5] = new Hexagon(TerrainType.FISHINGGROUND, 1);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			} else if (x == 2) {
				hexagons[x][0] = new Hexagon(TerrainType.FISHINGGROUND, 1);
				hexagons[x][1] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][2] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][3] = new Hexagon(TerrainType.LAKE, 0);
				hexagons[x][4] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][5] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			} else if (x == 3) {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][2] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][3] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][4] = new Hexagon(TerrainType.FISHINGGROUND, 1);
				hexagons[x][5] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			} else if (x == 4) {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][2] = new Hexagon(TerrainType.FISHINGGROUND, 1);
				hexagons[x][3] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][4] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][5] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			} else if (x == 5) {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][2] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][3] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][4] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][5] = new Hexagon(TerrainType.PASTURE, 1);
				hexagons[x][6] = new Hexagon(TerrainType.SEA, 0);
			} else if (x == 6) {
				hexagons[x][0] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][1] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][2] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][3] = new Hexagon(TerrainType.FISHINGGROUND, 1);
				hexagons[x][4] = new Hexagon(TerrainType.SEA, 0);
				hexagons[x][5] = new Hexagon(TerrainType.SEA, 0);
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
		Collections.shuffle(terrainPool);

		ArrayList<Integer> diceValues = new ArrayList<>();
		diceValues.addAll(Arrays.asList(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
		Collections.shuffle(diceValues);

		ArrayList<Integer> fishValues = new ArrayList<>();
		fishValues.addAll(Arrays.asList(4, 5, 6, 8, 9, 10));
		Collections.shuffle(fishValues);

		for (int x = 0; x < length; x++) {
			for (int y = 0; y < height; y++) {
				if (hexagons[x][y] != null) {
					if (hexagons[x][y].getType() != TerrainType.SEA && hexagons[x][y].getType() != TerrainType.LAKE
							&& hexagons[x][y].getType() != TerrainType.FISHINGGROUND) {
						TerrainType t = terrainPool.remove(0);
						hexagons[x][y].setType(t);
						hexagons[x][y].setNumber(diceValues.remove(0));
					} else if (hexagons[x][y].getType() == TerrainType.FISHINGGROUND) {
						hexagons[x][y].setNumber(fishValues.remove(0));
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

	public void setMerchantOwner(Player p) {
		merchantOwner = p;
	}

	public Player getMerchantOwner() {
		return merchantOwner;
	}

	public int bootDrawn() {
		return bootDrawn ? 0 : 1;
	}

	public void drewBoot() {
		bootDrawn = true;
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
		hexagons[1][1].getIntersection(IntersectionLoc.TOPRIGHT).setPortable(PortKind.ALLPORT);
		hexagons[1][1].getIntersection(IntersectionLoc.BOTTOMRIGHT).setPortable(PortKind.ALLPORT);
		hexagons[0][4].getIntersection(IntersectionLoc.TOP).setPortable(PortKind.BRICKPORT);
		hexagons[0][4].getIntersection(IntersectionLoc.TOPRIGHT).setPortable(PortKind.BRICKPORT);
		hexagons[2][6].getIntersection(IntersectionLoc.TOP).setPortable(PortKind.LUMBERPORT);
		hexagons[2][6].getIntersection(IntersectionLoc.TOPRIGHT).setPortable(PortKind.LUMBERPORT);
		hexagons[4][1].getIntersection(IntersectionLoc.TOPRIGHT).setPortable(PortKind.ALLPORT);
		hexagons[4][1].getIntersection(IntersectionLoc.BOTTOMRIGHT).setPortable(PortKind.ALLPORT);
		hexagons[4][5].getIntersection(IntersectionLoc.TOP).setPortable(PortKind.OREPORT);
		hexagons[4][5].getIntersection(IntersectionLoc.TOPRIGHT).setPortable(PortKind.OREPORT);
		hexagons[6][2].getIntersection(IntersectionLoc.TOPLEFT).setPortable(PortKind.WOOLPORT);
		hexagons[6][2].getIntersection(IntersectionLoc.BOTTOMLEFT).setPortable(PortKind.WOOLPORT);
		hexagons[3][2].getIntersection(IntersectionLoc.BOTTOM).setPortable(PortKind.GRAINPORT);
		hexagons[3][2].getIntersection(IntersectionLoc.BOTTOMLEFT).setPortable(PortKind.GRAINPORT);
		hexagons[6][5].getIntersection(IntersectionLoc.TOPLEFT).setPortable(PortKind.ALLPORT);
		hexagons[6][5].getIntersection(IntersectionLoc.BOTTOMLEFT).setPortable(PortKind.ALLPORT);
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

	public ArrayList<Edge> getEdges() {
		return edges;
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