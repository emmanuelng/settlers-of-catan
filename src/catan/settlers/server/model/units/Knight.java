package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class Knight implements IntersectionUnit {

	private static final long serialVersionUID = 1L;

	public static enum KnightType {
		BASIC_KNIGHT, STRONG_KNIGHT, MIGHTY_KNIGHT
	}

	private Player myOwner;
	private KnightType knightType;
	private boolean activated;

	private Cost buildKnightCost;
	private Cost updateKnightCost;
	public Cost activateKnightCost;

	public Knight(Player p) {
		this.myOwner = p;
		this.knightType = KnightType.BASIC_KNIGHT;
		this.activated = false;
		// need one wool and one ore to activate this kid

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
		Player p = this.getOwner();
		if (knightType == KnightType.BASIC_KNIGHT && p.canHire(KnightType.STRONG_KNIGHT)) {
			knightType = KnightType.STRONG_KNIGHT;
		}
		if (knightType == KnightType.STRONG_KNIGHT && p.canHire(KnightType.MIGHTY_KNIGHT)) {
			knightType = KnightType.MIGHTY_KNIGHT;
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

	public boolean isActive() {
		return activated;
	}

}
