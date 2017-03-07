package catan.settlers.server.view;

import java.util.ArrayList;

import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.units.IntersectionUnit;

public class Intersection {
	private Hexagon[] myHexes;
	private ArrayList<Edge> myEdges;
	private IntersectionUnit myUnit;
	
	public void addEdge(Edge e) { myEdges.add(e); }
}
