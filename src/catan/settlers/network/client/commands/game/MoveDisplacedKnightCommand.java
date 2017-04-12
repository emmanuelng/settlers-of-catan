package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Knight;

public class MoveDisplacedKnightCommand implements ServerToClientCommand {

	private static final long serialVersionUID = -6619870097347643068L;
	private Intersection knightLoc;
	private Knight knightToMove;

	public MoveDisplacedKnightCommand(Intersection knightLoc) {
		this.knightLoc = knightLoc;
		this.knightToMove = (Knight) knightLoc.getUnit();
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setSelectedIntersection(knightLoc);
		gsm.setMoveKnightMode(true);
		gsm.setShowSelectIntersectionLayer(true);
		gsm.setCanMoveKnightIntersecIds(knightToMove.canCanMoveIntersecIds());
		gsm.setdBox("Your knight has been displaced!", "Select a new intersection to move your displaced knight to.");
	}

}
