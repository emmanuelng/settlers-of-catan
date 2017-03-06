package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;

public interface IntersectionUnit {
	public void setOwner(Player p);
	public Player getOwner();
}
