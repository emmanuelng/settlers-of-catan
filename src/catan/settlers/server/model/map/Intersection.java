package catan.settlers.server.model.map;

import java.io.Serializable;
import java.util.HashSet;

import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Port.PortKind;

public class Intersection implements Serializable {

	private static final long serialVersionUID = 1L;
	private IntersectionUnit unit;

	private HashSet<Hexagon> myHexagons;
	private HashSet<Edge> myEdges;

	private int id;
	private boolean isPortable;
	private PortKind pkind;

	public Intersection(int id) {
		this.id = id;
		unit = null;
		myEdges = new HashSet<>();
		myHexagons = new HashSet<>();
	}

	public void addEdge(Edge e) {
		myEdges.add(e);
	}

	public void addHex(Hexagon h) {
		if (h != null) {
			if (!myHexagons.contains(h)) {
				myHexagons.add(h);
			}
		}
	}

	public HashSet<Hexagon> getHexagons() {
		HashSet<Hexagon> ret = new HashSet<>();
		for (Hexagon hex : myHexagons)
			ret.add(hex);
		return ret;
	}

	public HashSet<Edge> getEdges() {
		HashSet<Edge> ret = new HashSet<>();
		for (Edge edge : myEdges)
			ret.add(edge);
		return ret;
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

	public boolean canBuild(String username, GamePhase curPhase) {
		if (unit != null)
			return false;

		/**
		 * It is not possible to build a settlement on the sea
		 */
		if (isMaritime())
			return false;

		/**
		 * It is not possible to build two settlements on adjacent edges
		 */
		for (Edge e : myEdges) {
			Intersection opp = e.getOppIntersection(this);
			if (opp != null)
				if (opp.getUnit() != null)
					return false;
		}

		/**
		 * If it is not the setup phase, there should be a road owned by the
		 * player in the neighborhood
		 */
		for (Edge e : myEdges) {
			if (curPhase != GamePhase.TURNPHASE)
				return true;

			if (e.getOwner() != null) {
				if (e.getOwner().getUsername().equals(username)) {
					return true;
				}
			}

		}

		return false;
	}

	public boolean canBuild(Player player, GamePhase curPhase) {
		return canBuild(player.getUsername(), curPhase);
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

	public void setPortable(PortKind p) {
		isPortable = true;
		pkind = p;
	}

	public PortKind getPortKind() {
		return pkind;
	}
}
