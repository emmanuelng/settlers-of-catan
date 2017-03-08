package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;

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
		drawRectangle(MinuetoColor.WHITE, 0, 0, 500, 500);
		visitedHexes = new HashMap<>();
		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				drawHex(board.getHexagonAt(x, y), 70 + x * HexagonImage.SIZE, 70 + y * HexagonImage.SIZE);
			}
		}
		drawDice(450, 450);
	}

	private void drawDice(int x, int y) {
		MinuetoImage dice;
		try {
			dice = new MinuetoImageFile("images/dice.png");
		} catch (MinuetoFileException e) {
			System.out.println("Could not load image file");
			return;
		}
		this.draw(dice, x, y);
	}

	private void drawHex(Hexagon hex, int x, int y) {
		if (hex == null)
			return;

		if (visitedHexes.get(hex) != null)
			return;

		HexagonImage image = new HexagonImage(hex);
		draw(image, x, y);
		visitedHexes.put(hex, true);

		int hexsize = image.getHexSize();
		int s = hexsize;
		int t = (int) hexsize / 2;
		int r = (int) (hexsize * 0.8660254037844);

		drawHex(board.getHexNeighborInDir(hex, Direction.WEST), x - r - r, y);
		drawHex(board.getHexNeighborInDir(hex, Direction.NORTHWEST), x - r, y - s - t);
		drawHex(board.getHexNeighborInDir(hex, Direction.NORTHEAST), x + r, y - s - t);
		drawHex(board.getHexNeighborInDir(hex, Direction.EAST), x + r + r, y);
		drawHex(board.getHexNeighborInDir(hex, Direction.SOUTHEAST), x + r, y + s + t);
		drawHex(board.getHexNeighborInDir(hex, Direction.SOUTHWEST), x - r, y + s + t);

		drawEdges(hex, x, y);
	}

	private void drawEdges(Hexagon hex, int x, int y) {
		if (hex == null)
			return;

		draw(new EdgeImage(hex.getEdge(Direction.WEST)).rotate(Math.PI / 2), 20 + x, 30 + y);
		draw(new EdgeImage(hex.getEdge(Direction.NORTHWEST)).rotate(-30 * Math.PI / 180), 27 + x, y);
		draw(new EdgeImage(hex.getEdge(Direction.NORTHEAST)).rotate(30 * Math.PI / 180), 70 + x, y);
		draw(new EdgeImage(hex.getEdge(Direction.EAST)).rotate(Math.PI / 2), 105 + x, 30 + y);
		draw(new EdgeImage(hex.getEdge(Direction.SOUTHEAST)).rotate(-30 * Math.PI / 180), 70 + x, 75 + y);
		draw(new EdgeImage(hex.getEdge(Direction.SOUTHWEST)).rotate(30 * Math.PI / 180), 25 + x, 75 + y);
	}

}
