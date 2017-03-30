package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;

public class Knight implements IntersectionUnit {

	private static final long serialVersionUID = 1L;

	public static enum KnightType {
		BASIC_KNIGHT, STRONG_KNIGHT, MIGHTY_KNIGHT
	}

	private Player myOwner;
	private KnightType knightType;
	private boolean activated;

	public Knight(Player p) {
		if (p.canHire(KnightType.BASIC_KNIGHT)) {
			myOwner = p;
			knightType = KnightType.BASIC_KNIGHT;
			activated = false; // need one wool and one ore to activate this kid
		}
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

	public KnightType getKnightType() {
		return knightType;
	}

	private void setKnightType(KnightType kType) {
		knightType = kType;
	}

	public void activateKnight() {
		// TODO: need to pay one grain to activate
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
