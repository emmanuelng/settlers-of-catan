package catan.settlers.server.model.units;

import java.io.Serializable;
import java.util.HashMap;

import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class Cost implements Serializable {

	private static final long serialVersionUID = -6688086084862384407L;
	private HashMap<ResourceType, Integer> price;

	public Cost() {
		this.price = new HashMap<>();
	}

	public Cost(Cost cost) {
		this.price = new HashMap<>();
		for (ResourceType rtype : cost.price.keySet()) {
			this.price.put(rtype, cost.price.get(rtype));
		}
	}

	public void addPriceEntry(ResourceType rtype, int amt) {
		price.put(rtype, amt);
	}

	public boolean canPay(Player player) {
		HashMap<ResourceType, Integer> playerResources = player.getResources();
		for (ResourceType rtype : price.keySet()) {
			if (playerResources.get(rtype) < price.get(rtype))
				return false;
		}
		return true;
	}

	public void removeResources(Player player) {
		for (ResourceType rtype : price.keySet()) {
			player.removeResource(rtype, price.get(rtype));
		}
	}
}
