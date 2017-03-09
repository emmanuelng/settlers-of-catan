package catan.settlers.server.model.units;

import java.io.Serializable;

import catan.settlers.server.model.Player;

public interface IntersectionUnit extends Serializable {
	public Player getOwner();
	public boolean isKnight();
	public boolean isVillage();
}
