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
		if (canHire(KnightType.BASIC_KNIGHT)) {
			myOwner = p;
			knightType = KnightType.BASIC_KNIGHT;
			activated = false; // need one wool and one ore to activate this kid
		}
	}

	public void upgradeKnight(Knight k) {
		if (k.getKnightType() == KnightType.BASIC_KNIGHT && canHire(KnightType.STRONG_KNIGHT)) {
			k.setKnightType(KnightType.STRONG_KNIGHT);
		}
		if (k.getKnightType() == KnightType.STRONG_KNIGHT && canHire(KnightType.MIGHTY_KNIGHT)) {
			k.setKnightType(KnightType.MIGHTY_KNIGHT);
		}
	}

	public boolean canHire(KnightType type) {
		if (type == KnightType.BASIC_KNIGHT) {
			if (myOwner.getKnightCount(type) < 2) {
				return true;
			}
			return false;
		} else if (type == KnightType.STRONG_KNIGHT) {
			if (myOwner.getKnightCount(type) < 2) {
				return true;
			}
			return false;
		} else if (type == KnightType.MIGHTY_KNIGHT) {
			if (myOwner.getKnightCount(type) < 2) {
				return true;
			}
			return false;
		}
		return true;
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
