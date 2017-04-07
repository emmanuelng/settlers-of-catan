package catan.settlers.server.model;

import java.io.Serializable;
import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.ProgressCards.ProgressCardType;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Knight;

/**
 * This class represents an Turn. For any game action, the client needs to build
 * a TurnData object and send it to the server. The server then extracts the
 * required data and does the appropriate actions (see the receiveResponse
 * method in the Game class)
 *
 */

public class TurnData implements Serializable {

	private static final long serialVersionUID = -2490523320154300533L;

	public static enum TurnAction {
		BUILD_SETTLEMENT, BUILD_KNIGHT, BUILD_ROAD, UPGRADE_SETTLEMENT, UPGRADE_KNIGHT, BUILD_WALL, END_TURN, ACTIVATE_KNIGHT, PROGRESS_CARD, DISPLACE_KNIGHT, SEVEN_DISCARD, ROLL_DICE, BUILD_SHIP, RESOURCE_SELECTED, PLAYER_SELECTED, HEX_SELECTED
	}

	private Intersection selectedIntersection;
	private Edge selectedEdge;
	private TurnData.TurnAction myAction;
	private HashMap<ResourceType, Integer> sevenDiscardresources;
	private Knight selectedKnight;
	private ResourceType selectedResource;
	private ProgressCardType progressCard;
	private String selectedPlayer;
	private Hexagon selectedHex;

	public TurnData(TurnAction action) {
		ClientModel cm = ClientModel.instance;

		this.selectedIntersection = cm.getGameStateManager().getSelectedIntersection();
		this.selectedEdge = cm.getGameStateManager().getSelectedEdge();
		this.selectedKnight = cm.getGameStateManager().getSelectedKnight();
		this.myAction = action;
	}

	public Intersection getIntersectionSelection() {
		return selectedIntersection;
	}

	public Edge getEdgeSelection() {
		return selectedEdge;
	}

	public TurnAction getAction() {
		return myAction;
	}

	public void setSevenDiscardResources(HashMap<ResourceType, Integer> resources) {
		if (sevenDiscardresources == null)
			sevenDiscardresources = new HashMap<>();

		for (ResourceType rtype : ResourceType.values()) {
			int amt = resources.get(rtype) == null ? 0 : resources.get(rtype);
			sevenDiscardresources.put(rtype, amt);
		}

	}

	public HashMap<ResourceType, Integer> getSevenResources() {
		return sevenDiscardresources;
	}

	public Knight getSelectedKnight() {
		return selectedKnight;
	}

	public void setSelectedResource(ResourceType rtype) {
		this.selectedResource = rtype;
	}

	public ResourceType getSelectedResourceOrCommodity() {
		return selectedResource;
	}

	public void setProgressCard(ProgressCardType pCardType) {
		this.progressCard = pCardType;
	}

	public ProgressCardType getProgressCard() {
		return progressCard;
	}

	public void setSelectedPlayer(String username) {
		this.selectedPlayer = username;
	}

	public String getSelectedPlayer() {
		return selectedPlayer;
	}
	
	public void setSelectedHex(Hexagon hex) {
		this.selectedHex = hex;
	}

	public Hexagon getSelectedHex() {
		return selectedHex;
	}
}
