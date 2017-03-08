package catan.settlers.client.view.game;

import java.util.HashMap;

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
		visitedHexes = new HashMap<>();
		drawHex(board.getHexagonAt(0, 0), 20, 20);
		drawDice(450,450);
	}
	
	private void drawDice(int x, int y){
		MinuetoImage dice;
		try{
			dice = new MinuetoImageFile("images/dice.png");
		}catch (MinuetoFileException e) {
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
		
		int hexsize= image.getHexSize();
		int s = hexsize;
		int t = (int) hexsize/2;
		int r = (int) (hexsize * 0.8660254037844);
		
		drawHex(board.getHexNeighborInDir(hex, Direction.WEST), x - r - r, y);
		drawHex(board.getHexNeighborInDir(hex, Direction.NORTHWEST), x - r , y - s - t );
		drawHex(board.getHexNeighborInDir(hex, Direction.NORTHEAST), x + r , y - s - t);
		drawHex(board.getHexNeighborInDir(hex, Direction.EAST), x + r + r , y);
		drawHex(board.getHexNeighborInDir(hex, Direction.SOUTHEAST), x + r, y + s + t);
		drawHex(board.getHexNeighborInDir(hex, Direction.SOUTHWEST), x - r, y + s + t);
	}

}
