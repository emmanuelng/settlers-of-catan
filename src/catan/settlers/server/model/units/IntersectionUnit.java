package catan.settlers.server.model.units;

import java.io.Serializable;

import catan.settlers.server.model.Player;

public interface IntersectionUnit extends Serializable {
	public void setOwner(Player p);
	public Player getOwner();
}
