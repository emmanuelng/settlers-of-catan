package catan.settlers.server.model.units;

import java.util.HashSet;

import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.Intersection;

public class Knight implements IntersectionUnit {

	private static final long serialVersionUID = -2624908793631834956L;

	public static enum KnightType {
		BASIC_KNIGHT, STRONG_KNIGHT, MIGHTY_KNIGHT
	}

	private Player myOwner;
	private KnightType knightType;
	private boolean activated;

	private Cost buildKnightCost;
	private Cost updateKnightCost;
	public Cost activateKnightCost;
	private Intersection locatedAt;

	private HashSet<Edge> visitedEdges;

	public Knight(Player p, Intersection initialLocation) {
		this.myOwner = p;
		this.knightType = KnightType.BASIC_KNIGHT;
		this.activated = false;
		this.locatedAt = initialLocation;

		this.buildKnightCost = new Cost();
		buildKnightCost.addPriceEntry(ResourceType.ORE, 1);
		buildKnightCost.addPriceEntry(ResourceType.WOOL, 1);

		this.updateKnightCost = new Cost();
		updateKnightCost.addPriceEntry(ResourceType.GRAIN, 1);
		updateKnightCost.addPriceEntry(ResourceType.WOOL, 1);

		this.activateKnightCost = new Cost();
		activateKnightCost.addPriceEntry(ResourceType.GRAIN, 1);
	}

	public Cost getBuildKnightCost() {
		return new Cost(buildKnightCost);
	}

	public Cost getUpdateKnightCost() {
		return new Cost(updateKnightCost);
	}

	public Cost getActivateKnightCost() {
		return new Cost(activateKnightCost);
	}

	public void upgradeKnight() {
		switch (knightType) {
		case BASIC_KNIGHT:
			knightType = KnightType.STRONG_KNIGHT;
			break;
		case STRONG_KNIGHT:
			knightType = KnightType.MIGHTY_KNIGHT;
			break;
		case MIGHTY_KNIGHT:
			break;
		}
	}

	@Override
	public Player getOwner() {
		return myOwner;
	}

	public KnightType getType() {
		return knightType;
	}

	public void activateKnight() {
		activated = true;
	}

	@Override
	public boolean isKnight() {
		return true;
	}

	@Override
	public boolean isVillage() {
		return false;
	}

	@Override
	public Intersection getLocatedAt() {
		return locatedAt;
	}

	public boolean isActive() {
		return activated;
	}

	public void setLocatedAt(Intersection newLocation) {
		this.locatedAt = newLocation;
	}

	/**
	 * Returns a list of the intersection ids where the knight can be moved
	 */
	public HashSet<Integer> canCanMoveIntersecIds() {
		visitedEdges = new HashSet<>();
		HashSet<Integer> ret = new HashSet<>();

		for (Edge edge : locatedAt.getEdges()) {
			ret.addAll(checkEdge(edge));
		}

		return ret;
	}

	/**
	 * Given an edges, checks if the knight can move on its intersections, and
	 * propagates the check to neighbor edges
	 */
	private HashSet<Integer> checkEdge(Edge edge) {
		HashSet<Integer> ret = new HashSet<>();

		if (visitedEdges.contains(edge)) {
			return ret;
		} else {
			visitedEdges.add(edge);
		}

		if (edge.getOwner() == null) {
			return ret;
		} else {
			if (!edge.getOwner().getUsername().equals(myOwner.getUsername()))
				return ret;
		}

		Intersection intersections[] = edge.getIntersections();

		if (intersections[0].getUnit() == null && !intersections[0].isMaritime())
			ret.add(intersections[0].getId());

		if (intersections[1].getUnit() == null && !intersections[1].isMaritime())
			ret.add(intersections[1].getId());

		for (Edge e : intersections[0].getEdges()) {
			if (e != edge)
				ret.addAll(checkEdge(e));
		}

		for (Edge e : intersections[1].getEdges()) {
			if (e != edge)
				ret.addAll(checkEdge(e));
		}

		return ret;
	}

}
