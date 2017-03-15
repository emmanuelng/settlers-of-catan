package catan.settlers.server.model;

import java.io.Serializable;

import catan.settlers.client.model.ClientModel;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.Intersection;

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

	public TurnData(ClientModel clientModel) {
		this.selectedIntersection = clientModel.getGameStateManager().getSelectedIntersection();
		this.selectedEdge = clientModel.getGameStateManager().getSelectedEdge();
	}

	public Intersection getIntersectionSelection() {
		return selectedIntersection;
	}

	public Edge getEdgeSelection() {
		return selectedEdge;
	}
}
