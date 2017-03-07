package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.image.MinuetoImage;

import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.Direction;

public class GameBoardImage extends MinuetoImage {

	private GameBoard board;
	private HashMap<Hexagon, Boolean> visitedHexes;

	public GameBoardImage(GameBoard board) {
		super(500, 500);

		this.board = board;
		compose();
	}

	private void compose() {
		visitedHexes = new HashMap<>();
		drawHex(board.getHexagonAt(0, 0), 20, 20);
	}

	private void drawHex(Hexagon hex, int x, int y) {
		if (hex == null)
			return;

		if (visitedHexes.get(hex) != null)
			return;

		HexagonImage image = new HexagonImage(hex);
		draw(image, x, y);
		visitedHexes.put(hex, true);

		int hexWidth = 100;
		int hexHeight = 100;

		drawHex(board.getHexNeighborInDir(hex, Direction.WEST), x - hexWidth, y);
		drawHex(board.getHexNeighborInDir(hex, Direction.NORTHWEST), x - hexWidth / 2, y - (3 * hexHeight) / 4);
		drawHex(board.getHexNeighborInDir(hex, Direction.NORTHEAST), x + hexWidth / 2, y - (3 * hexHeight) / 4);
		drawHex(board.getHexNeighborInDir(hex, Direction.EAST), x + hexWidth, y);
		drawHex(board.getHexNeighborInDir(hex, Direction.SOUTHEAST), x + hexWidth, y + (3 * hexHeight) / 2);
		drawHex(board.getHexNeighborInDir(hex, Direction.SOUTHWEST), x - hexWidth / 2, y + (3 * hexHeight) / 4);
	}

}
