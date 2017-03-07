package catan.settlers.server.model.map;

import java.io.Serializable;

import catan.settlers.server.view.Intersection;

public class Hex implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum TerrainType {
		SEA, DESERT, PASTURE, FOREST, MOUNTAIN, HILLS, FIELD, GOLDMINE
	}

	private Edge[] myEdges;
	private Intersection[] myIntersections;
	private int num;
	private TerrainType myType;

	public Hex() {
		num = 0;
		myType = null;

	}

	public void setType(TerrainType t) {
		myType = t;
	}

	public TerrainType getType() {
		return myType;
	}

	public void setNum(int n) {
		num = n;
	}

	public int getNum() {
		return num;
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
	
	public int getResource() {
		switch (myType) { 
			case PASTURE:
				return 4;
			case FOREST:
				return 2;
			case MOUNTAIN:
				return 3;
			case HILLS: 
				return 0;
			case FIELD:
				return 1;
			default:
				return -1;
		}
	}
	
	public int getCommodity() {
		switch (myType) {	
			case PASTURE:
				return 5;
			case FOREST:
				return 7;
			case MOUNTAIN:
				return 6;
			case GOLDMINE:
				return 6;
			case HILLS: 
				return 0;
			case FIELD:
				return 1;
			default:
				return -1;
		}
	}
}
