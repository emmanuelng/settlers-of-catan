package catan.settlers.server.model;

import java.io.Serializable;
import java.util.HashMap;

import catan.settlers.client.model.ClientModel;
import catan.settlers.server.model.Game.turnAction;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Edge;
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

	private static final long serialVersionUID = 1L;
	private Intersection selectedIntersection;
	private Edge selectedEdge;
	private turnAction myAction;
	private HashMap<ResourceType, Integer> sevenDiscardresources;
	private Knight selectedKnight;

	public TurnData(ClientModel clientModel) {
		this.selectedIntersection = clientModel.getGameStateManager().getSelectedIntersection();
		this.selectedEdge = clientModel.getGameStateManager().getSelectedEdge();
		this.selectedKnight = clientModel.getGameStateManager().getSelectedKnight();
	}

	public Intersection getIntersectionSelection() {
		return selectedIntersection;
	}

	public Edge getEdgeSelection() {
		return selectedEdge;
	}

	public void setAction(turnAction t) {
		myAction = t;
	}

	public turnAction getAction() {
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
}
