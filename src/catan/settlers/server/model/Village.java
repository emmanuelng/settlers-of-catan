package catan.settlers.server.model;

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
