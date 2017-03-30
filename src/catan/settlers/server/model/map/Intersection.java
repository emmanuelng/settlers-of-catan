package catan.settlers.server.model.map;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.server.model.Player;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Port.PortKind;

public class Intersection implements Serializable {

	private static final long serialVersionUID = 1L;
	private IntersectionUnit unit;

	private ArrayList<Hexagon> myHexagons;
	private ArrayList<Edge> myEdges;

	private int id;
	private boolean isPortable;
	private PortKind pkind;

	public Intersection(int id) {
		this.id = id;
		unit = null;
		myEdges = new ArrayList<Edge>();
		myHexagons = new ArrayList<Hexagon>();
	}

	public void addEdge(Edge e) {
		if (e != null && myEdges.size() < 3) {
			if (!myEdges.contains(e)) {
				myEdges.add(e);
			}
		}
	}

	public void addHex(Hexagon h) {
		if (h != null) {
			if (!myHexagons.contains(h)) {
				myHexagons.add(h);
			}
		}
	}

	public ArrayList<Hexagon> getHexagons() {
		return myHexagons;
	}

	public ArrayList<Edge> getEdges() {
		return myEdges;
	}

	public IntersectionUnit getUnit() {
		return unit;
	}

	public void setUnit(IntersectionUnit unit) {
		this.unit = unit;
	}

	public int getId() {
		return id;
	}

	public boolean canBuild() {
		if (unit != null) {
			return false;
		}
		for (Edge e : myEdges) {
			Intersection opp = e.getOppIntersection(this);
			if (opp != null) {
				if (opp.getUnit() != null) {
					return false;
				}
			}
		}
		for (Hexagon hex : myHexagons) {
			if (hex.getType() != Hexagon.TerrainType.SEA) {
				return true;
			}
		}
		return false;
	}

	public boolean connected(Player p) {
		if (unit == null) {
			for (Edge e : myEdges) {
				if (e.getOwner() == p) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isMaritime() {
		for (Hexagon hex : myHexagons) {
			if (hex.getType() != Hexagon.TerrainType.SEA) {
				return false;
			}
		}
		return true;
	}

	public boolean isPortable() {
		return isPortable;
	}
	
	public void setPortable(PortKind p){
		isPortable = true;
		pkind= p;
	}
	
	public PortKind getPortKind() {
		return pkind;
	}
}
