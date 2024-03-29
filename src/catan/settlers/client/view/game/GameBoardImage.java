package catan.settlers.client.view.game;

import java.util.HashSet;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoCircle;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.ImageFileManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.Direction;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Village;

public class GameBoardImage extends ImageLayer {

	private GameBoard board;

	public GameBoardImage() {
		super();
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doUpdateBoard())
			return;

		clear();
		board = ClientModel.instance.getGameStateManager().getBoard();
		if (board == null)
			return;

		int offsetX = (ClientWindow.WINDOW_WIDTH / 2) - ((board.getLength() * HexagonImage.WIDTH) / 2);
		int offsetY = (ClientWindow.WINDOW_HEIGHT / 2) - ((board.getLength() * HexagonImage.HEIGHT) / 2) + 25;

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

			if (hex == board.getRobberHex()) {
				MinuetoImage robber = ClientModel.instance.getImageFileManager().load("images/robber.png");
				draw(robber, x + hexImg.getWidth() / 2 - robber.getWidth() / 2,
						y + hexImg.getHeight() / 2 - robber.getHeight() / 2 + 3);
			}
			if (hex == board.getMerchantHex()) {
				MinuetoImage merchant = ClientModel.instance.getImageFileManager().load("images/merchant1.png");
				draw(merchant, x + hexImg.getWidth() / 2 - merchant.getWidth() / 2,
						y + hexImg.getHeight() / 2 - merchant.getHeight() / 2 + 3);
			}

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

		GameStateManager gsm = ClientModel.instance.getGameStateManager();

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

			boolean isSelected = ClientModel.instance.getGameStateManager()
					.getSelectedIntersection() == curIntersection;
			IntersectionImage intersecImg = IntersectionImage.getIntersectionImage(curIntersection, isSelected);

			int posX = x + shift_x - intersecImg.getWidth() / 2;
			int posY = y + shift_y - intersecImg.getWidth() / 2;

			if (curIntersection.getUnit() != null) {
				if (curIntersection.getUnit().isVillage()) {
					Village v = (Village) curIntersection.getUnit();
					if (v.hasWalls()) {
						String wallOwner = v.getOwner().getUsername();
						MinuetoColor wallsColor = ClientWindow.getInstance().getGameWindow()
								.getColorByUsername(wallOwner);
						MinuetoRectangle walls = new MinuetoRectangle(26, 26, wallsColor, true);
						MinuetoRectangle wallsBorder = new MinuetoRectangle(26, 26, wallsColor.darken(0.2), false);
						draw(walls, posX - 3, posY - 3);
						draw(wallsBorder, posX - 3, posY - 3);
					}
				}
			}

			draw(intersecImg, posX, posY);

			// If the game is in move knight mode, display the intersections
			// where the knight can move
			if (gsm.isMoveKnightMode()) {
				HashSet<Integer> canMoveIntersecIds = gsm.getCanMoveKnightIntersecIds();
				if (canMoveIntersecIds.contains(curIntersection.getId())) {
					MinuetoCircle canMoveIntersecImage = new MinuetoCircle(10, MinuetoColor.GREEN, true);
					draw(canMoveIntersecImage, posX, posY);
				}
			}

			// If there is a knight, draw it on top
			if (curIntersection.getUnit() instanceof Knight) {
				GameWindow gw = ClientWindow.getInstance().getGameWindow();
				ImageFileManager imf = ClientModel.instance.getImageFileManager();
				Knight knight = (Knight) curIntersection.getUnit();
				int playerNo = gw.getPlayerNumber(knight.getOwner().getUsername());
				boolean isActive = knight.isActive();

				String path = "images/";
				path += isActive ? "active_" : "inactive_";
				path += knight.getType() + "_p" + playerNo;

				MinuetoImage knightImage = imf.load(path + ".png");
				draw(knightImage, x + shift_x - knightImage.getWidth() / 2, y + shift_y - knightImage.getHeight() / 2);
			}

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
			int posX = x + shift_x - 5;
			int posY = y + shift_y;

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
