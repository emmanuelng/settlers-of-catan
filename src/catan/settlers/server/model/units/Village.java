package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;
import catan.settlers.server.model.map.VillageKind;

public class Village implements IntersectionUnit {
	private Player myOwner;
	private VillageKind myKind;
	
	public Village(Player p) {
		myOwner = p;
		myKind = VillageKind.SETTLEMENT;
	}
	
	public Player getOwner() { return myOwner; }
	public void setOwner(Player p) { myOwner = p; }
	public VillageKind getKind() { return myKind; }
}
