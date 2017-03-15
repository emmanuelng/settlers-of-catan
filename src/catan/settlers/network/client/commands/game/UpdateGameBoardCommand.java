package catan.settlers.network.client.commands.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
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
		ClientModel.instance.getGameStateManager().setBoard(board);
	}

}
