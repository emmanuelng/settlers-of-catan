package catan.settlers.client.view.game;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.Session;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.Intersection;

public class SendSettlementRoadSelectionCommand implements ClientToServerCommand {

	private static final long serialVersionUID = 1L;
	private int gameId;
	private Intersection i;
	private Edge e;
	
	public SendSettlementRoadSelectionCommand(int gameId, Intersection i, Edge e) {
		this.gameId= gameId;
	}
	
	@Override
	public void execute(Session sender, Server server) {
		Game g = server.getGameManager().getGameById(gameId);
		Intersection intersectionSelection = g.getGameBoardManager().getBoard().getIntersectionById(i.getId());
		Edge edgeSelection = g.getGameBoardManager().getBoard().getEdgeById(e.getId());
		if (edgeSelection.hasIntersection(intersectionSelection) && edgeSelection.getUnit() == null && ) {
			Player p = server.getPlayerManager().getPlayerBySession(sender);
			g.placeSettlement(intersectionSelection, p);
			g.placeRoad(edgeSelection, p);
			
			// send success message, disable buttons
		} else {
			// send fail message, ask to try again
		}
	}
}
