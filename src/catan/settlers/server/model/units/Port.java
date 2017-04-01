package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;

public class Port extends Village {

	private static final long serialVersionUID = 1L;

	public enum PortKind {
		ALLPORT, BRICKPORT, LUMBERPORT, OREPORT, WOOLPORT, WHEATPORT
	}

	private PortKind myKind;

	public Port(Player p) {
		super(p);
	}

	public PortKind getPortKind() {
		return myKind;
	}

}
