package catan.settlers.server.model;

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
public class TurnData {
	
	private Intersection selectedIntersection;
	private Edge selectedEdge;

	public TurnData(ClientModel clientModel) {
		this.selectedIntersection = clientModel.getCurrentIntersection();
		this.selectedEdge = clientModel.getCurrentEdge();
	}

	public Intersection getIntersectionSelection() {
		return selectedIntersection;
	}

	public Edge getEdgeSelection() {
		return selectedEdge;
	}
}
