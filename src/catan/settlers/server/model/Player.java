package catan.settlers.server.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.ProgressCards.ProgressCardType;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.units.Knight.KnightType;
import catan.settlers.server.model.units.Port.PortKind;

public class Player implements Serializable {

	private static final long serialVersionUID = -7339361279458070804L;

	public enum ResourceType {
		BRICK, GRAIN, LUMBER, ORE, WOOL, CLOTH, COIN, PAPER
	}

	private HashMap<ResourceType, Integer> resources;
	private HashMap<ProgressCardType, Integer> progressCards;
	private HashMap<PortKind, Boolean> ownedPorts;
	private int numberOfWalls;

	private Credentials credentials;
	private int basicKnightCount, strongKnightCount, mightyKnightCount;
	private int tradeImprovement, politicsImprovement, scienceImprovement;

	private boolean medicine;
	private boolean engineer;
	private boolean crane;
	private int smith;
	private int roadBuilding;
	private int victoryP;
	private ResourceType currentlySelectedResource;
	private ArrayList<ResourceType> tradeAtAdvantage;
	private int fishCount;
	private boolean hasBoot;

	private Hexagon selectedHex;

	public Player(Credentials credentials) {
		this.credentials = credentials;
		this.resources = new HashMap<>();
		this.ownedPorts = new HashMap<>();
		this.progressCards = new HashMap<>();
		this.tradeAtAdvantage = new ArrayList<>();

		// this will be removed later
		this.tradeImprovement = 0;
		this.politicsImprovement = 0;
		this.scienceImprovement = 0;
		this.victoryP = 0;

		for (ResourceType resType : ResourceType.values()) {
			resources.put(resType, 12);
		}

		for (PortKind portKind : PortKind.values()) {
			ownedPorts.put(portKind, false);
		}

		for (ProgressCardType pctype : ProgressCardType.values()) {
			progressCards.put(pctype, 0);
		}
	}

	public int getVP() {
		return victoryP;
	}

	public void incrementVP(int amount) {
		victoryP = victoryP + amount;
	}

	public void decrementVP(int amount) {
		if (victoryP >= amount) {
			victoryP = victoryP - amount;
		}
		// no way of knowing if we called this if we try to decrement more than
		// num of vp
	}

	public boolean hasPortOfThisResource(ResourceType r) {
		switch (r) {
		case WOOL:
			return hasPort(PortKind.WOOLPORT);
		case BRICK:
			return hasPort(PortKind.BRICKPORT);
		case GRAIN:
			return hasPort(PortKind.GRAINPORT);
		case LUMBER:
			return hasPort(PortKind.LUMBERPORT);
		case ORE:
			return hasPort(PortKind.OREPORT);
		default:
			return false;
		}
	}

	public boolean hasPort(PortKind portKind) {
		return ownedPorts.get(portKind);
	}

	public void setPort(PortKind portKind) {
		ownedPorts.put(portKind, true);
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public String getUsername() {
		return credentials.getUsername();
	}

	public HashMap<ResourceType, Integer> getResources() {
		HashMap<ResourceType, Integer> res = new HashMap<>();
		for (ResourceType type : resources.keySet())
			res.put(type, resources.get(type));
		return res;
	}

	public int getResourceAmount(ResourceType res) {
		return resources.get(res);
	}

	public void giveResource(ResourceType r, int amount) {
		if (r == null)
			return;

		int currentAmount = resources.get(r);
		resources.put(r, currentAmount + amount);
	}

	public boolean removeResource(ResourceType r, int amount) {
		if (r == null)
			return false;

		int previous = resources.get(r);

		if (previous - amount >= 0) {
			resources.put(r, previous - amount);
			return true;
		}

		return false;
	}

	public int getNbResourceCards() {
		int count = 0;
		for (ResourceType rt : ResourceType.values()) {
			count += resources.get(rt);
		}
		return count;
	}

	public int getNumFish(){
		return fishCount;
	}
	
	public void giveFish(int n) {
		fishCount += n;
	}

	public void removeFish(int n) {
		if (n <= fishCount) {
			fishCount -= n;
		}
	}

	// If a player has the old boot, they need an additional VP to win!
	public void giveBoot() {
		hasBoot = true;
	}

	public void removeBoot() {
		hasBoot = false;
	}

	public void giveProgressCard(ProgressCardType pc) {
		int currentAmt = progressCards.get(pc);
		progressCards.put(pc, currentAmt + 1);
	}

	public HashMap<ProgressCardType, Integer> getProgressCards() {
		HashMap<ProgressCardType, Integer> ret = new HashMap<>();
		ret.putAll(progressCards);
		return ret;
	}

	public void useProgressCard(ProgressCardType pc) {
		int prvAmt = progressCards.get(pc);

		if (prvAmt > 0)
			progressCards.put(pc, prvAmt - 1);
	}

	public int getKnightCount(KnightType kType) {
		if (kType == KnightType.BASIC_KNIGHT) {
			return basicKnightCount;
		} else if (kType == KnightType.STRONG_KNIGHT) {
			return strongKnightCount;
		} else if (kType == KnightType.MIGHTY_KNIGHT) {
			return mightyKnightCount;
		}
		return 0;
	}

	public void setKnightCount(KnightType kType, int kCount) {
		if (kType == KnightType.BASIC_KNIGHT) {
			basicKnightCount = kCount;
		} else if (kType == KnightType.STRONG_KNIGHT) {
			strongKnightCount = kCount;
		} else if (kType == KnightType.MIGHTY_KNIGHT) {
			mightyKnightCount = kCount;
		}
	}

	public void setNumberOfWalls(int num) {
		numberOfWalls = num;
	}

	public int getNumberOfWalls() {
		return numberOfWalls;
	}

	public int getTradeLevel() {
		return tradeImprovement;
	}

	public int getPoliticsLevel() {
		return politicsImprovement;
	}

	public int getScienceLevel() {
		return scienceImprovement;
	}

	// Can't build mighty knights without barracks
	public boolean hasBarracks() {
		return (politicsImprovement >= 3);
	}

	// can trade any commodity at 2:1
	public boolean hasMerchantGuild() {
		return (tradeImprovement >= 3);
	}

	// if player doesn't draw on a roll, can choose a resource
	public boolean hasAqueduct() {
		return (scienceImprovement >= 3);
	}

	public boolean hasMedicine() {
		return medicine;
	}

	public void playMedicine() {
		medicine = true;
	}

	public void useMedicine() {
		medicine = false;
	}

	public boolean hasEngineer() {
		return engineer;
	}

	public void playEngineer() {
		engineer = true;
	}

	public void useEngineer() {
		engineer = false;
	}

	public boolean hasCrane() {
		return crane;
	}

	public void playCrane() {
		crane = true;
	}

	public void useCrane() {
		crane = false;
	}

	public boolean hasSmith() {
		return (smith > 0);
	}

	public void playSmith() {
		smith = 2;
	}

	public void useSmith() {
		smith--;
	}

	public boolean hasRoadBuilding() {
		return (roadBuilding > 0);
	}

	public void playRoadBuilding() {
		roadBuilding = 2;
	}

	public void useRoadBuilding() {
		roadBuilding--;
	}

	public boolean canHire(KnightType type) {
		if (getKnightCount(type) < 2) {
			return true;
		}
		return false;
	}

	public Session getSession() {
		return Server.getInstance().getAuthManager().getSessionByCredentials(credentials);
	}

	public void sendCommand(ServerToClientCommand cmd) {
		Session s = Server.instance.getAuthManager().getSessionByCredentials(credentials);
		try {
			s.sendCommand(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			Player other = (Player) obj;
			return credentials.getUsername().equals(other.credentials.getUsername());
		}
		return false;
	}

	public HashMap<PortKind, Boolean> getOwnedPorts() {
		HashMap<PortKind, Boolean> ret = new HashMap<>();
		for (PortKind pkind : PortKind.values()) {
			ret.put(pkind, ownedPorts.get(pkind));
		}
		return ret;
	}

	public void setCurrentSelectedResource(ResourceType resource) {
		this.currentlySelectedResource = resource;
	}

	public ResourceType getCurrentSelectedResource() {
		return currentlySelectedResource;
	}

	public void setTradeAtAdvantage(ResourceType resource) {
		tradeAtAdvantage.add(resource);
	}

	public ArrayList<ResourceType> getTradeAtAdvantage() {
		ArrayList<ResourceType> ret = new ArrayList<>();
		ret.addAll(tradeAtAdvantage);
		return ret;
	}

	public void removeTradeAtAdvantage(ArrayList<ResourceType> resource) {
		tradeAtAdvantage.remove(resource);
	}

	public void resetTradeAtAdvantage() {
		tradeAtAdvantage = new ArrayList<>();
	}

	public ResourceType drawRandomResource() {
		ResourceType ret = null;
		while (ret == null) {
			int resourceIndex = new Random().nextInt(ResourceType.values().length);
			ResourceType rtype = ResourceType.values()[resourceIndex];
			if (resources.get(rtype) > 0)
				ret = rtype;
		}

		return ret;
	}

	public boolean hasCommodities() {
		return resources.get(ResourceType.COIN) > 0 || resources.get(ResourceType.CLOTH) > 0
				|| resources.get(ResourceType.PAPER) > 0;
	}

	public void setCurrentSelectedHex(Hexagon selectedHex) {
		this.selectedHex = selectedHex;
	}

	public Hexagon getCurrentSelectedHex() {
		return selectedHex;
	}

	public void setTradeLvl(int lvl) {
		tradeImprovement = lvl;
	}

	public void setPoliticsLvl(int lvl) {
		politicsImprovement = lvl;
	}

	public void setScienceLvl(int lvl) {
		scienceImprovement = lvl;
	}

	public void addFreeRoad() {
		roadBuilding++;
	}
	
	public void setHasBoot(boolean b){
		hasBoot = b;
	}
	
	public boolean getHasBoot(){
		return hasBoot;
	}
}