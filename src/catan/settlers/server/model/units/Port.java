package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;
import catan.settlers.server.model.map.Intersection;

public class Port extends Village {

	private static final long serialVersionUID = -5470531702052541981L;

	public enum PortKind {
		ALLPORT, BRICKPORT, LUMBERPORT, OREPORT, WOOLPORT, GRAINPORT
	}

	private PortKind myKind;

	public Port(Player p, Intersection location) {
		super(p, location);
	}

	public PortKind getPortKind() {
		return myKind;
	}

}
