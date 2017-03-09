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

	public VillageKind getKind() {
		return myKind;
	}

	public void upgradeToCity() {
		myKind = VillageKind.CITY;
	}

	@Override
	public boolean isKnight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVillage() {
		// TODO Auto-generated method stub
		return true;
	}
}
