package catan.settlers.server.view;

import java.util.ArrayList;

import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Village;

public class Intersection {
	private ArrayList<Intersection> myNeighbours;
	private ArrayList<Edge> myEdges;
	private IntersectionUnit myUnit;
	
	public void addEdge(Edge e) { 
		if (!myEdges.contains(e)) {
			myEdges.add(e); 
		}
	}
	
	public void addIntersection(Intersection i) {
		if (!myNeighbours.contains(i)) {
			myNeighbours.add(i);
		}
	}
	
	public IntersectionUnit getUnit() { return myUnit; }
	
	public void setUnit(IntersectionUnit u) {
		myUnit = u;
	}
	
	public boolean canBuild() {
		for (Intersection i : myNeighbours) {
			IntersectionUnit u = i.getUnit();
			if (u instanceof Village) {
				return false;
			}
		}
		return true;
	}
}
