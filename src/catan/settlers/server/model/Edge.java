package catan.settlers.server.model;

import java.util.ArrayList;

public class Edge {
	// Left and right is arbitrary; simply showing that neighbouring edges are connects at one side or another
	private ArrayList<Edge> leftEdges;
	private ArrayList<Edge> rightEdges;
	private Hex[] myHexes;
	private Intersection[] myIntersections;
	private boolean hasRoad;
	
	public Edge() {
		hasRoad = false;
	}
	
	public void addLeftEdge(Edge e) { leftEdges.add(e); }
	public void addRightEdge(Edge e) { rightEdges.add(e); }
	
	public void setIntersection(Intersection t, int i) { myIntersections[i] = t; }
	public Intersection getIntersection(int i) { return myIntersections[i]; }
}
