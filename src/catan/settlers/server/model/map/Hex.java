package catan.settlers.server.model.map;

import catan.settlers.server.view.Intersection;

public class Hex {
	
	private Edge[] myEdges;
	private Intersection[] myIntersections;
	private int num;
	private TerrainType myType;
	
	public Hex() {
		num = 0;
		myType = null;
		
	}
	
	public void setType(TerrainType t) { myType = t; }
	public TerrainType getType() { return myType; }
	
	public void setNum(int n) { num = n; }
	public int getNum() { return num; }
	
	public void setEdge(Edge e, int i) { myEdges[i] = e; }	
	public Edge getEdge(int i) { return myEdges[i]; }
	
	public void setIntersection(Intersection t, int i) { myIntersections[i] = t; }
	public Intersection getIntersection(int i) { return myIntersections[i]; }

}
