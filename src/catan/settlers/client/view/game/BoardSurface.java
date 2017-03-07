package catan.settlers.client.view.game;

import org.minueto.image.MinuetoImage;

import catan.settlers.client.view.game.EdgeImage.EdgeType;
import catan.settlers.client.view.game.HexagonImage.HexType;
import catan.settlers.server.model.map.GameBoard;

public class BoardSurface extends MinuetoImage {
	private int[] intersections;
	private int sizeX, sizeY;

	public BoardSurface(int sizeX, int sizeY) {
		super(sizeX, sizeY);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	private void drawHex(int posX, int posY, HexType type) {
		HexagonImage hex = new HexagonImage(type);
		this.draw(hex.getHexImage(), posX, posY);
		for (int i = 0; i < hex.drawCoordinates(posX, posY).length; i += 2) {
			int x0 = hex.drawCoordinates(posX, posY)[i];
			int y0 = hex.drawCoordinates(posX, posY)[i + 1];
			this.drawIntersection(x0, y0);
		}
		for (int i = 0; i < hex.drawCoordinates(posX, posY).length - 3; i += 4) {
			int x0 = hex.drawCoordinates(posX, posY)[i];
			int y0 = hex.drawCoordinates(posX, posY)[i + 1];
			int xf = hex.drawCoordinates(posX, posY)[i + 2];
			int yf = hex.drawCoordinates(posX, posY)[i + 3];
			if (xf > x0 && yf > y0) {
				this.drawRoad(x0, y0, xf, yf, EdgeType.NORTHEAST);
			} else if (xf > x0 && yf < y0) {
				this.drawRoad(x0, y0, xf, yf, EdgeType.SOUTHEAST);
			} else if (xf > x0 && yf == y0) {
				this.drawRoad(x0, y0, xf, yf, EdgeType.EAST);
			} else if (xf < x0 && yf < y0) {
				this.drawRoad(x0, y0, xf, yf, EdgeType.SOUTHWEST);
			} else if (xf < x0 && yf > y0) {
				this.drawRoad(x0, y0, xf, yf, EdgeType.NORTHWEST);
			} else if (xf < x0 && yf == y0) {
				this.drawRoad(x0, y0, xf, yf, EdgeType.WEST);
			}
		}
	}

	private void drawIntersection(int posX, int posY) {
		/*
		 * 5 is half of the intersection's size, so the center of intersection
		 * is at the vertex
		 */

		IntersectionImage intersection = new IntersectionImage();
		this.draw(intersection.getIntersectionImage(), posX - 5, posY - 5);
	}

	private void drawRoad(int startX, int startY, int endX, int endY, EdgeType etype) {
		EdgeImage edge = new EdgeImage(startX, startY, endX, endY, etype);
		this.draw(edge.getEdgeImage(), 0, 0);
	}

	public void drawHexGrid(int x, int y, GameBoard board) {
		this.drawHex(x, y + 90 * 1, HexType.BRICK);
		this.drawHex(x, y + 90 * 2, HexType.BRICK);
		this.drawHex(x, y + 90 * 3, HexType.BRICK);

		this.drawHex(x + 80, y - 45 + 90 * 1, HexType.BRICK);
		this.drawHex(x + 80, y - 45 + 90 * 2, HexType.BRICK);
		this.drawHex(x + 80, y - 45 + 90 * 3, HexType.BRICK);
		this.drawHex(x + 80, y - 45 + 90 * 4, HexType.BRICK);

		this.drawHex(x + 160, y - 90 + 90 * 1, HexType.BRICK);
		this.drawHex(x + 160, y - 90 + 90 * 2, HexType.BRICK);
		this.drawHex(x + 160, y - 90 + 90 * 3, HexType.BRICK);
		this.drawHex(x + 160, y - 90 + 90 * 4, HexType.BRICK);
		this.drawHex(x + 160, y - 90 + 90 * 5, HexType.BRICK);

		this.drawHex(x + 240, y - 45 + 90 * 1, HexType.BRICK);
		this.drawHex(x + 240, y - 45 + 90 * 2, HexType.BRICK);
		this.drawHex(x + 240, y - 45 + 90 * 3, HexType.BRICK);
		this.drawHex(x + 240, y - 45 + 90 * 4, HexType.BRICK);

		this.drawHex(x + 320, y + 90 * 1, HexType.BRICK);
		this.drawHex(x + 320, y + 90 * 2, HexType.BRICK);
		this.drawHex(x + 320, y + 90 * 3, HexType.BRICK);
	}

	public int getX() {
		return sizeX;
	}

	public int getY() {
		return sizeY;
	}
}
