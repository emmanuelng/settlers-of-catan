package catan.settlers.server.model.map;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.server.model.units.IntersectionUnit;

public class Intersection implements Serializable {

	private static final long serialVersionUID = 1L;
	private IntersectionUnit unit;
	
	private ArrayList<Edge> myEdges;
	
	private int id;
	
	public Intersection(int id) {
		this.id = id;
		unit = null;
		myEdges = new ArrayList<Edge>();
	}
	
	public void addEdge(Edge e) {
		if (e != null) {
			if (!myEdges.contains(e)) {
				myEdges.add(e);
			}
		}
	}

	public IntersectionUnit getUnit() {
		return unit;
	}
	
	public void setUnit(IntersectionUnit unit) {
		this.unit = unit;
	}

}
