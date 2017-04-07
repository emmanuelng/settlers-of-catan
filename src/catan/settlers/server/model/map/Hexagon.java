package catan.settlers.server.model.map;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class Hexagon implements Serializable {

	private static final long serialVersionUID = 2796066592140868855L;
	private static int currentId = 0;

	public enum TerrainType {
		SEA, DESERT, PASTURE, FOREST, MOUNTAIN, HILLS, FIELD, GOLDMINE
	}

	public enum Direction {
		WEST, NORTHWEST, NORTHEAST, EAST, SOUTHEAST, SOUTHWEST
	}

	public enum IntersectionLoc {
		TOPLEFT, TOP, TOPRIGHT, BOTTOMRIGHT, BOTTOM, BOTTOMLEFT
	}

	
	
	private Edge[] myEdges;
	private Intersection[] myIntersections;
	private int number;
	private TerrainType type;
	private int id;

	public Hexagon(TerrainType type, int number) {
		this.number = number;
		this.type = type;
		this.myEdges = new Edge[Direction.values().length];
		this.myIntersections = new Intersection[IntersectionLoc.values().length];
		this.id = currentId++;
	}

	public void setType(TerrainType t) {
		type = t;
	}

	public TerrainType getType() {
		return type;
	}

	
	public void setNumber(int n) {
		number = n;
	}

	public int getNumber() {
		return number;
	}
	
	public int getId() {
		return id;
	}


	public void setEdge(Edge e, Direction dir) {
		myEdges[dir.ordinal()] = e;
	}

	public Edge getEdge(Direction dir) {
		return myEdges[dir.ordinal()];
	}

	public void setIntersection(Intersection t, IntersectionLoc loc) {
		myIntersections[loc.ordinal()] = t;
	}

	public Intersection getIntersection(IntersectionLoc loc) {
		return myIntersections[loc.ordinal()];
	}

	
	
	/**
	 * Get the opposite direction of an edge (e.g. the WEST edge of a hex
	 * corresponds to the EAST edge of its neighbor)
	 */
	public static Direction getOppositeDir(Direction dir) {
		switch (dir) {
		case WEST:
			return Direction.EAST;
		case NORTHWEST:
			return Direction.SOUTHEAST;
		case NORTHEAST:
			return Direction.SOUTHWEST;
		case EAST:
			return Direction.WEST;
		case SOUTHEAST:
			return Direction.NORTHWEST;
		default:
			return Direction.NORTHEAST;
		}
	}

	/**
	 * Get the directions of the neighbors that share an intersection (e.g. if
	 * we take the TOPLEFT intersection of a hex, it is shared with its WEST and
	 * NORTHWEST neighbors)
	 */
	public static Direction[] getAdjacentDirs(IntersectionLoc loc) {
		Direction[] dirs = new Direction[2];

		switch (loc) {
		case TOPLEFT:
			dirs[0] = Direction.WEST;
			dirs[1] = Direction.NORTHWEST;
			break;
		case TOP:
			dirs[0] = Direction.NORTHEAST;
			dirs[1] = Direction.NORTHWEST;
			break;
		case TOPRIGHT:
			dirs[0] = Direction.NORTHEAST;
			dirs[1] = Direction.EAST;
			break;
		case BOTTOMRIGHT:
			dirs[0] = Direction.EAST;
			dirs[1] = Direction.SOUTHEAST;
			break;
		case BOTTOM:
			dirs[0] = Direction.SOUTHWEST;
			dirs[1] = Direction.SOUTHEAST;
			break;
		default:
			dirs[0] = Direction.WEST;
			dirs[1] = Direction.SOUTHWEST;
			break;
		}

		return dirs;
	}

	/**
	 * Get the corresponding intersection location in the neighbor
	 */
	public static IntersectionLoc getOppositeIntersection(IntersectionLoc loc, Direction dir) {
		switch (loc) {
		case TOPLEFT:
			return dir == Direction.WEST ? IntersectionLoc.TOPRIGHT : IntersectionLoc.BOTTOM;
		case TOP:
			return dir == Direction.NORTHEAST ? IntersectionLoc.BOTTOMLEFT : IntersectionLoc.BOTTOMRIGHT;
		case TOPRIGHT:
			return dir == Direction.NORTHEAST ? IntersectionLoc.BOTTOM : IntersectionLoc.TOPLEFT;
		case BOTTOMRIGHT:
			return dir == Direction.EAST ? IntersectionLoc.BOTTOMLEFT : IntersectionLoc.TOP;
		case BOTTOM:
			return dir == Direction.SOUTHWEST ? IntersectionLoc.TOPRIGHT : IntersectionLoc.TOPLEFT;
		default:
			return dir == Direction.WEST ? IntersectionLoc.BOTTOMRIGHT : IntersectionLoc.TOP;
		}
	}

	/**
	 * Get the corresponding resource, given a terrain type
	 */
	public static ResourceType terrainToResource(TerrainType t) {
		switch (t) {
		case FOREST:
			return ResourceType.LUMBER;
		case MOUNTAIN:
			return ResourceType.ORE;
		case PASTURE:
			return ResourceType.WOOL;
		case HILLS:
			return ResourceType.BRICK;
		case FIELD:
			return ResourceType.GRAIN;
		default:
			return null;
		}
	}

	public ArrayList<Edge> getEdges() {
		ArrayList<Edge> res = new ArrayList<>();
		for (Edge e : myEdges) {
			res.add(e);
		}
		return res;
	}

}
