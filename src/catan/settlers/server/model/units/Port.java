package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;

public class Port extends Village implements IntersectionUnit{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum PortKind{
		ALLPORT, BRICKPORT, LUMBERPORT, OREPORT, SHEEPPORT, WHEATPORT
	}

	private Player myOwner;
	private PortKind myKind;
	
	public Port(Player p){
		super(p);
	}
	
	@Override
	public Player getOwner() {
		// TODO Auto-generated method stub
		return myOwner;
	}
	
	public PortKind getPortKind(){
		return myKind;
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
