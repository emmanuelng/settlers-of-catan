package catan.settlers.server.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.server.model.ProgressCards.ProgressCardType;
import catan.settlers.server.model.units.Knight.KnightType;
import catan.settlers.server.model.units.Port.PortKind;

public class Player implements Serializable {

	/*private HashMap<ResourceType, Integer> resources;
	private ArrayList<ProgressCardType> ProgressCards;
	private HashMap<PortKind, Boolean> ownedPorts;
	
	private Credentials credentials;
	private int basicKnightCount, strongKnightCount, mightyKnightCount;
	private int tradeImprovement, politicsImprovement, scienceImprovement;

	public Player(Credentials credentials) {
		this.credentials = credentials;
		this.resources = new HashMap<>();
		this.ownedPorts = new HashMap<>();
		
		for (ResourceType resType : ResourceType.values()) {
			resources.put(resType, 12);
		}
		for (PortKind portKind : PortKind.values()) {
			ownedPorts.put(portKind, false);
		}
	}

	public boolean hasPort(PortKind portKind){
		return ownedPorts.get(portKind);
	}*/
	
	
	
	
	
	public enum ResourceType {
		BRICK, GRAIN, LUMBER, ORE, WOOL, CLOTH, COIN, PAPER
	}

	private static final long serialVersionUID = 1L;
	
	private HashMap<ResourceType, Integer> resources;
	private ArrayList<ProgressCardType> ProgressCards;
	private HashMap<PortKind, Boolean> ownedPorts;
	
	private Credentials credentials;
	private int basicKnightCount, strongKnightCount, mightyKnightCount;
	private int tradeImprovement, politicsImprovement, scienceImprovement;
	
	private boolean medicine;
	private boolean engineer;
	private boolean crane;
	private int smith;
	private int roadBuilding;

	public Player(Credentials credentials) {
		this.credentials = credentials;
		this.resources = new HashMap<>();
		this.ownedPorts = new HashMap<>();

		for (ResourceType resType : ResourceType.values()) {
			resources.put(resType, 12);
		}
		for (PortKind portKind : PortKind.values()) {
			ownedPorts.put(portKind, false);
		}
	}

	public boolean hasPort(PortKind portKind){
		return ownedPorts.get(portKind);
	}
	
	public void setPort(PortKind portKind){
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
		int currentAmount = resources.get(r);
		resources.put(r, currentAmount + amount);
	}

	public void removeResource(ResourceType r, int amount) {
		int previous = resources.get(r);
		resources.put(r, previous - amount);
	}

	public int getNbResourceCards() {
		int count = 0;
		for (ResourceType rt : ResourceType.values()) {
			count += resources.get(rt);
		}
		return count;
	}
	
	public void giveProgressCard(ProgressCardType pc) {
		ProgressCards.add(pc);
	}
	
	public void useProgressCard(ProgressCardType pc) {
		ProgressCards.remove(pc);
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
		return (roadBuilding >0 );
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

}