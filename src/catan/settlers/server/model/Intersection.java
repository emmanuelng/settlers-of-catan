package catan.settlers.server.model;

import java.util.ArrayList;

public class Intersection {
	private Hex[] myHexes;
	private ArrayList<Edge> myEdges;
	private IntersectionUnit myUnit;
	
	public void addEdge(Edge e) { myEdges.add(e); }
}
