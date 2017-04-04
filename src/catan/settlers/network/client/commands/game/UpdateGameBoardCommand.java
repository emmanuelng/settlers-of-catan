package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.network.client.commands.ServerToClientCommand;
import catan.settlers.server.model.map.GameBoard;

public class UpdateGameBoardCommand implements ServerToClientCommand {

	private static final long serialVersionUID = 1L;
	private GameBoard board;

	public UpdateGameBoardCommand(GameBoard board) {
		this.board = board;
	}

	@Override
	public void execute() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();

		gsm.setBoard(board);
		gsm.setMoveKnightMode(false);
		gsm.setSelectedEdge(null);
		gsm.setSelectedHex(null);
		gsm.setSelectedIntersection(null);
	}

}
