package catan.settlers.server.model.map;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.server.model.Player;
import catan.settlers.server.view.Intersection;

public class Edge implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Edge> leftEdges;
	private ArrayList<Edge> rightEdges;
	private Intersection[] myIntersections;
	private Player hasRoad;
	
	public Edge() {
		hasRoad = null;
	}
	
	public void addLeftEdge(Edge e) { leftEdges.add(e); }
	public void addRightEdge(Edge e) { rightEdges.add(e); }
	
	public void setIntersection(Intersection t, int i) { myIntersections[i] = t; }
	public Intersection getIntersection(int i) { return myIntersections[i]; }
	
	public void buildRoad(Player p) {
		if (hasRoad == null) {
			hasRoad = p;
		}
	}
}
