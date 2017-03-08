package catan.settlers.server.model.map;

import java.io.Serializable;

import catan.settlers.server.view.Intersection;

public class Hexagon implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum TerrainType {
		SEA, DESERT, PASTURE, FOREST, MOUNTAIN, HILLS, FIELD, GOLDMINE
	}

	public enum Direction {
		WEST, NORTHWEST, NORTHEAST, EAST, SOUTHEAST, SOUTHWEST
	}

	public enum IntersectionLocation {
		TOPLEFT, TOP, TOPRIGHT, BOTTOMRIGHT, BOTTOM, BOTTOMLEFT
	}

	private Edge[] myEdges;
	private Intersection[] myIntersections;
	private int number;
	private TerrainType type;

	public Hexagon(TerrainType type, int number) {
		this.number = number;
		this.type = type;
		this.myEdges = new Edge[Direction.values().length];
		this.myIntersections = new Intersection[IntersectionLocation.values().length];
	}

	public TerrainType getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}

	public void setEdge(Edge e, Direction dir) {
		myEdges[dir.ordinal()] = e;
	}

	public Edge getEdge(Direction dir) {
		return myEdges[dir.ordinal()];
	}

	public void setIntersection(Intersection t, IntersectionLocation loc) {
		myIntersections[loc.ordinal()] = t;
	}

	public Intersection getIntersection(IntersectionLocation loc) {
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
	public Direction[] getAdjacentDirs(IntersectionLocation loc) {
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
	public static IntersectionLocation getOppositeIntersection(IntersectionLocation loc, Direction dir) {
		switch (loc) {
		case TOPLEFT:
			if (dir == Direction.WEST) {
				return IntersectionLocation.TOPRIGHT;
			} else {
				
			}
		case TOP:
		case TOPRIGHT:
		case BOTTOMRIGHT:
		case BOTTOM:
		case BOTTOMLEFT:
		default:
			return null;
		}
	}

}
