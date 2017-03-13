package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.Direction;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.client.model.ClientModel;

public class GameBoardImage extends MinuetoImage {

	private GameBoard board;
	private HashMap<Hexagon, Boolean> visitedHexes;
	private HashMap<Edge, Hexagon> visitedEdges;
	private HashMap<Intersection, Boolean> visitedIntersections;

	public GameBoardImage(GameBoard board) {
		super(1366, 700);
		this.board = board;
		compose();
	}

	private void compose() {
		clear(MinuetoColor.WHITE);
		visitedHexes = new HashMap<>();
		visitedEdges = new HashMap<>();
		visitedIntersections = new HashMap<>();

		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				drawHex(board.getHexagonAt(x, y), 70 + x * 1366, 70 + y * 700);
			}
		}
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
		drawIntersections(hex, x, y);
	}

	private void drawIntersections(Hexagon hex, int x, int y) {
		if (hex == null)
			return;

		for (IntersectionLoc loc : IntersectionLoc.values()) {
			Intersection curIntersection = hex.getIntersection(loc);

			if (visitedIntersections.get(curIntersection) != null)
				continue;

			int shift_x = 0, shift_y = 0;
			switch (loc) {
			case TOPLEFT:
				shift_x = 15;
				shift_y = 15;
				break;
			case TOP:
				shift_x = 58;
				shift_y = -7;
				break;
			case TOPRIGHT:
				shift_x = 100;
				shift_y = 15;
				break;
			case BOTTOMRIGHT:
				shift_x = 100;
				shift_y = 67;
				break;
			case BOTTOM:
				shift_x = 58;
				shift_y = 90;
				break;
			default:
				shift_x = 15;
				shift_y = 67;
				break;
			}

			IntersectionImage intersecImg = new IntersectionImage(shift_x + x, shift_y + y, curIntersection);
			draw(intersecImg, shift_x + x, shift_y + y);
			ClientWindow.getInstance().getGameWindow().getMouseHandler().register(intersecImg);
		}
	}

	private void drawEdges(Hexagon hex, int x, int y) {
		if (hex == null)
			return;

		for (Direction dir : Direction.values()) {
			Edge curEdge = hex.getEdge(dir);

			if (visitedEdges.keySet().contains(curEdge))
				continue;

			double rotation;
			int shift_x = 0, shift_y = 0;

			switch (dir) {
			case WEST:
				rotation = Math.PI / 2;
				shift_x = 20;
				shift_y = 30;
				break;
			case NORTHWEST:
				rotation = -30 * Math.PI / 180;
				shift_x = 27;
				shift_y = 0;
				break;
			case NORTHEAST:
				rotation = 30 * Math.PI / 180;
				shift_x = 70;
				shift_y = 0;
				break;
			case EAST:
				rotation = Math.PI / 2;
				shift_x = 105;
				shift_y = 30;
				break;
			case SOUTHEAST:
				rotation = -30 * Math.PI / 180;
				shift_x = 70;
				shift_y = 75;
				break;
			default:
				rotation = 30 * Math.PI / 180;
				shift_x = 25;
				shift_y = 75;
				break;
			}

			MinuetoColor edgeMColor = new MinuetoColor(230, 230, 230);

			if (curEdge.getOwner() == null) {
				if (curEdge != ClientModel.instance.getCurrentEdge()) {
					if (!curEdge.isMaritime()) {
						edgeMColor = new MinuetoColor(230, 230, 230);
					} else {
						edgeMColor = new MinuetoColor(170, 218, 255);
					}
				} else {
					edgeMColor = MinuetoColor.RED;
				}
			} else {
				String username = curEdge.getOwner().getUsername();
				edgeMColor = ClientWindow.getInstance().getGameWindow().getColorByUsername(username);
			}

			EdgeImage edgeImg = new EdgeImage(curEdge, rotation, shift_x + x, shift_y + y, edgeMColor);
			draw(edgeImg.rotate(rotation), shift_x + x, shift_y + y);

			ClientWindow.getInstance().getGameWindow().getMouseHandler().register(edgeImg);
			visitedEdges.put(curEdge, hex);
		}
	}

}
