package catan.settlers.network.client.commands.game;

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
		// TODO: Call the board rendering method
	}

}
