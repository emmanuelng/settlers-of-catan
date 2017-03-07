package catan.settlers.server.model;

import java.io.Serializable;

import catan.settlers.server.model.map.GameBoard;

public class GameBoardManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private GameBoard board;

	public GameBoardManager() {
		this.board = new GameBoard();
	}

	public GameBoard getBoard() {
		return board;
	}

}
