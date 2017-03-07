package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;

public class Village implements IntersectionUnit {

	private static final long serialVersionUID = 1L;

	public enum VillageKind {
		SETTLEMENT, CITY, METROPOLIS
	}

	private Player myOwner;
	private VillageKind myKind;

	public Village(Player p) {
		myOwner = p;
		myKind = VillageKind.SETTLEMENT;
	}

	public Player getOwner() {
		return myOwner;
	}

	public void setOwner(Player p) {
		myOwner = p;
	}

	public VillageKind getKind() {
		return myKind;
	}
	
	public void upgradeToCity() {
		myKind = VillageKind.CITY;
	}
}
