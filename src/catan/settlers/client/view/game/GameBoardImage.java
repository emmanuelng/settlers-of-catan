package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.Direction;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Intersection;

public class GameBoardImage extends MinuetoImage {

	private GameBoard board;

	public GameBoardImage() {
		super(ClientWindow.WINDOW_WIDTH, ClientWindow.WINDOW_HEIGHT);
		compose();
	}

	public void compose() {
		board = ClientModel.instance.getGameStateManager().getBoard();
		if (board == null)
			return;

		int offsetX = (int) (((board.getLength() + 0.5) * HexagonImage.WIDTH) / 2);
		int offsetY = 60;

		clear(MinuetoColor.WHITE);
		int hex_height = HexagonImage.HEIGHT, hex_width = HexagonImage.WIDTH;
		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				int pos_x = (int) (x * hex_width + 0.5 * hex_width * (1 - (y % 2))) + offsetX;
				int pos_y = (int) (y * 0.75 * hex_height) + offsetY;
				drawHex(board.getHexagonAt(x, y), pos_x, pos_y);
				drawEdges(board.getHexagonAt(x, y), pos_x, pos_y);
			}
		}

		/*
		 * Do a second loop to make sure that the intersection are the latest
		 * registered clickables
		 */
		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				int pos_x = (int) (x * hex_width + 0.5 * hex_width * (1 - (y % 2))) + offsetX;
				int pos_y = (int) (y * 0.75 * hex_height) + offsetY;
				drawIntersections(board.getHexagonAt(x, y), pos_x, pos_y);
			}
		}
	}

	private void drawHex(Hexagon hex, int x, int y) {
		if (hex == null)
			return;

		HexagonImage image = HexagonImage.getHexagonImage(hex);
		draw(image, x, y);
	}

	private void drawIntersections(Hexagon hex, int x, int y) {
		if (hex == null)
			return;

		for (IntersectionLoc loc : IntersectionLoc.values()) {
			Intersection curIntersection = hex.getIntersection(loc);

			int shift_x = 0, shift_y = 0;
			switch (loc) {
			case TOPLEFT:
				shift_x = 0;
				shift_y = (int) (0.25 * HexagonImage.HEIGHT);
				break;
			case TOP:
				shift_x = (int) ((0.5 * HexagonImage.WIDTH));
				shift_y = 5;
				break;
			case TOPRIGHT:
				shift_x = HexagonImage.WIDTH;
				shift_y = (int) (0.25 * HexagonImage.HEIGHT);
				break;
			case BOTTOMRIGHT:
				shift_x = HexagonImage.WIDTH;
				shift_y = (int) (0.75 * HexagonImage.HEIGHT) + 5;
				break;
			case BOTTOM:
				shift_x = (int) ((0.5 * HexagonImage.WIDTH));
				shift_y = HexagonImage.HEIGHT;
				break;
			default:
				shift_x = 0;
				shift_y = (int) (0.75 * HexagonImage.HEIGHT) + 5;
				break;
			}

			int posX = (int) (x + shift_x - 0.5 * (IntersectionImage.SIZE));
			int posY = (int) (y + shift_y - 0.5 * (IntersectionImage.SIZE));

			boolean isSelected = ClientModel.instance.getGameStateManager()
					.getSelectedIntersection() == curIntersection;
			IntersectionImage intersecImg = IntersectionImage.getIntersectionImage(curIntersection, posX, posY,
					isSelected);
			draw(intersecImg, posX, posY);
			ClientWindow.getInstance().getGameWindow().getMouseHandler().register(intersecImg);
		}
	}

	private void drawEdges(Hexagon hex, int x, int y) {
		if (hex == null)
			return;

		for (Direction dir : Direction.values()) {
			Edge curEdge = hex.getEdge(dir);

			double rotation;
			int shift_x = 0, shift_y = 0;

			switch (dir) {
			case WEST:
				rotation = Math.PI / 2;
				shift_x = 0;
				shift_y = (int) (0.25 * HexagonImage.HEIGHT);
				break;
			case NORTHWEST:
				rotation = -30 * Math.PI / 180;
				shift_x = 0;
				shift_y = 0;
				break;
			case NORTHEAST:
				rotation = 30 * Math.PI / 180;
				shift_x = (int) (0.5 * HexagonImage.WIDTH);
				shift_y = 0;
				break;
			case EAST:
				rotation = Math.PI / 2;
				shift_x = HexagonImage.WIDTH;
				shift_y = (int) (0.25 * HexagonImage.HEIGHT);
				break;
			case SOUTHEAST:
				rotation = -30 * Math.PI / 180;
				shift_x = (int) (0.5 * HexagonImage.WIDTH);
				shift_y = (int) (0.75 * HexagonImage.HEIGHT);
				break;
			default:
				rotation = 30 * Math.PI / 180;
				shift_x = 0;
				shift_y = (int) (0.75 * HexagonImage.HEIGHT);
				break;
			}

			int posX = (int) (x + shift_x - 0.1 * EdgeImage.getWidthByRotation(rotation));
			int posY = (int) (y + shift_y - 0.1 * EdgeImage.getHeightByRotation(rotation));
			boolean isSelected = curEdge == ClientModel.instance.getGameStateManager().getSelectedEdge();

			EdgeImage edgeImg = EdgeImage.getEdgeImage(curEdge, rotation, posX, posY, isSelected);
			draw(edgeImg.rotate(rotation), posX, posY);

			ClientWindow.getInstance().getGameWindow().getMouseHandler().register(edgeImg);
		}
	}

}
