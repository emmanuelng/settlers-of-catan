package catan.settlers.server.model.units;

import catan.settlers.network.client.commands.game.FailureCommand;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Intersection;

public class Village implements IntersectionUnit {

	private static final long serialVersionUID = 349286583534399001L;

	public enum VillageKind {
		SETTLEMENT, CITY, METROPOLIS
	}

	private Player myOwner;
	private VillageKind myKind;

	private Cost buildSettlementCost;
	private Cost upgradeToCityCost;
	private Cost buildWallCost;
	private boolean hasWall;
	private Intersection locatedAt;

	public Village(Player p, Intersection location) {
		this.myOwner = p;
		this.myKind = VillageKind.SETTLEMENT;
		this.locatedAt = location;
		hasWall = false;

		this.buildSettlementCost = new Cost();
		buildSettlementCost.addPriceEntry(ResourceType.BRICK, 1);
		buildSettlementCost.addPriceEntry(ResourceType.GRAIN, 1);
		buildSettlementCost.addPriceEntry(ResourceType.LUMBER, 1);
		buildSettlementCost.addPriceEntry(ResourceType.WOOL, 1);

		this.upgradeToCityCost = new Cost();
		upgradeToCityCost.addPriceEntry(ResourceType.ORE, 3);
		upgradeToCityCost.addPriceEntry(ResourceType.GRAIN, 2);

		this.buildWallCost = new Cost();
		if (!myOwner.hasEngineer()) {
			buildWallCost.addPriceEntry(ResourceType.BRICK, 2);
		}
	}

	@Override
	public Player getOwner() {
		return myOwner;
	}

	public VillageKind getKind() {
		return myKind;
	}

	public void upgradeToCity() {
		myKind = VillageKind.CITY;
	}

	public void destroyCity() {
		myKind = VillageKind.SETTLEMENT;
	}

	public void buildWall() {
		if (this.getKind() == VillageKind.SETTLEMENT) {
			myOwner.sendCommand(new FailureCommand("Cannot build walls on a settlement"));
		} else if (myOwner.getNumberOfWalls() >= 3) {
			myOwner.sendCommand(new FailureCommand("You cannot build walls anymore"));
		} else {
			this.hasWall = true;
			myOwner.setNumberOfWalls(myOwner.getNumberOfWalls() + 1);
		}
	}

	public Cost getBuildSettlementCost() {
		return new Cost(buildSettlementCost);
	}

	public Cost getUpgradeToCityCost() {
		return new Cost(upgradeToCityCost);
	}

	public Cost getbuildWallCost() {
		return new Cost(buildWallCost);
	}

	@Override
	public boolean isKnight() {
		return false;
	}

	@Override
	public boolean isVillage() {
		return true;
	}

	@Override
	public Intersection getLocatedAt() {
		return locatedAt;
	}

	public boolean hasWalls() {
		return hasWall;
	}

	@Override
	public void setOwner(Player newOwner) {
		myOwner = newOwner;
	}
}
