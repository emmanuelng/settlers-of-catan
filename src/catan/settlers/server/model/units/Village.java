package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class Village implements IntersectionUnit {

	private static final long serialVersionUID = 1L;

	public enum VillageKind {
		SETTLEMENT, CITY, METROPOLIS
	}

	private Player myOwner;
	private VillageKind myKind;

	private Cost buildSettlementCost;
	private Cost upgradeToCityCost;

	public Village(Player p) {
		this.myOwner = p;
		this.myKind = VillageKind.SETTLEMENT;

		this.buildSettlementCost = new Cost();
		buildSettlementCost.addPriceEntry(ResourceType.BRICK, 1);
		buildSettlementCost.addPriceEntry(ResourceType.GRAIN, 1);
		buildSettlementCost.addPriceEntry(ResourceType.LUMBER, 1);
		buildSettlementCost.addPriceEntry(ResourceType.WOOL, 1);
		
		this.upgradeToCityCost = new Cost();
		upgradeToCityCost.addPriceEntry(ResourceType.ORE, 3);
		upgradeToCityCost.addPriceEntry(ResourceType.GRAIN, 2);
	}

	public Player getOwner() {
		return myOwner;
	}

	public VillageKind getKind() {
		return myKind;
	}

	public void upgradeToCity() {
		myKind = VillageKind.CITY;
	}
	
	public Cost getBuildSettlementCost() {
		return new Cost(buildSettlementCost);
	}
	
	public Cost getUpgradeToCityCost() {
		return new Cost(upgradeToCityCost);
	}

	@Override
	public boolean isKnight() {
		return false;
	}

	@Override
	public boolean isVillage() {
		return true;
	}
}
