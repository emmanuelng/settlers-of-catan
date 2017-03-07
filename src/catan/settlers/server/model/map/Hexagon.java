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

	private Edge[] myEdges;
	private Intersection[] myIntersections;
	private int number;
	private TerrainType type;

	public Hexagon(TerrainType type, int number) {
		this.number = number;
		this.type = type;

	}

	public TerrainType getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}

	public void setEdge(Edge e, int i) {
		myEdges[i] = e;
	}

	public Edge getEdge(int i) {
		return myEdges[i];
	}

	public void setIntersection(Intersection t, int i) {
		myIntersections[i] = t;
	}

	public Intersection getIntersection(int i) {
		return myIntersections[i];
	}

}
