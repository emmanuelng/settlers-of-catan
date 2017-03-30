package catan.settlers.client.view.game;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.Direction;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Intersection;

public class GameBoardImage extends ImageLayer {

	private GameBoard board;
	
	public GameBoardImage() {
		super();
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doUpdateBoard())
			return;

		board = ClientModel.instance.getGameStateManager().getBoard();
		if (board == null)
			return;

		int offsetX = (int) (((board.getLength() + 0.5) * HexagonImage.WIDTH) / 2);
		int offsetY = 60;

		int hex_height = HexagonImage.HEIGHT, hex_width = HexagonImage.WIDTH;

		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				int pos_x = (int) (x * hex_width + 0.5 * hex_width * (1 - (y % 2))) + offsetX;
				int pos_y = (int) (y * 0.75 * hex_height) + offsetY;
				drawHex(board.getHexagonAt(x, y), pos_x, pos_y);
			}
		}

		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				int pos_x = (int) (x * hex_width + 0.5 * hex_width * (1 - (y % 2))) + offsetX;
				int pos_y = (int) (y * 0.75 * hex_height) + offsetY;
				drawEdges(board.getHexagonAt(x, y), pos_x, pos_y);
			}
		}

		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				int pos_x = (int) (x * hex_width + 0.5 * hex_width * (1 - (y % 2))) + offsetX;
				int pos_y = (int) (y * 0.75 * hex_height) + offsetY;
				drawIntersections(board.getHexagonAt(x, y), pos_x, pos_y);
			}
		}
	}

	private void drawHex(Hexagon hex, int x, int y) {
		if (hex != null) {
			HexagonImage hexImg = HexagonImage.getHexagonImage(hex);
			draw(hexImg, x, y);
			if (!hexImg.isSelectedImage()) {
				registerClickable(hexImg, new ClickListener() {
					@Override
					public void onClick() {
						GameStateManager gsm = ClientModel.instance.getGameStateManager();
						if (gsm.getSelectedHex() != hex) {
							gsm.setSelectedHex(hex);
						} else {
							gsm.setSelectedHex(null);
						}
					}
				});
			}
		}
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
			IntersectionImage intersecImg = IntersectionImage.getIntersectionImage(curIntersection, isSelected);
			draw(intersecImg, posX, posY);

			if (!intersecImg.isSelectedImage()) {
				registerClickable(intersecImg, new ClickListener() {
					@Override
					public void onClick() {
						GameStateManager gsm = ClientModel.instance.getGameStateManager();
						if (gsm.getSelectedIntersection() != intersecImg.getModel()) {
							gsm.setSelectedIntersection(intersecImg.getModel());
						} else {
							gsm.setSelectedIntersection(null);
						}
					}
				});
			}
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

			boolean isSelected = curEdge == ClientModel.instance.getGameStateManager().getSelectedEdge();
			EdgeImage edgeImg = EdgeImage.getEdgeImage(curEdge, rotation, isSelected);
			int posX = (int) (x + shift_x - 5);
			int posY = (int) (y + shift_y);

			draw(edgeImg.getImage(), posX, posY);

			if (!edgeImg.isSelectedImage()) {
				registerClickable(edgeImg.getImage(), new ClickListener() {
					@Override
					public void onClick() {
						GameStateManager gsm = ClientModel.instance.getGameStateManager();
						if (gsm.getSelectedEdge() != edgeImg.getModel()) {
							gsm.setSelectedEdge(edgeImg.getModel());
						} else {
							gsm.setSelectedEdge(null);
						}
					}
				});
			}
		}
	}
}
