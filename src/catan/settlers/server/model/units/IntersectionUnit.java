package catan.settlers.server.model.units;

import java.io.Serializable;

import catan.settlers.server.model.Player;
import catan.settlers.server.model.map.Intersection;

public interface IntersectionUnit extends Serializable {

	public Player getOwner();

	public Intersection getLocatedAt();

	public boolean isKnight();

	public boolean isVillage();

	public void setOwner(Player newOwner);
}
