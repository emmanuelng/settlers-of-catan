package catan.settlers.server.model;

import catan.settlers.client.model.ClientModel;

/**
 * This class represents an Turn. For any game action, the client needs to build
 * a TurnData object and send it to the server. The server then extracts the
 * required data and does the appropriate actions (see the receiveResponse
 * method in the Game class)
 *
 */
public class TurnData {
	
	public TurnData(ClientModel clientModel) {
		// TODO Populate this object with the client model
	}
}
