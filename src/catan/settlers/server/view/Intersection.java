package catan.settlers.server.view;

import java.util.ArrayList;

import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.Hex;
import catan.settlers.server.model.units.IntersectionUnit;

public class Intersection {
	private Hex[] myHexes;
	private ArrayList<Edge> myEdges;
	private IntersectionUnit myUnit;
	
	public void addEdge(Edge e) { myEdges.add(e); }
}
