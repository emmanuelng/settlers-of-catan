package catan.settlers.client.view.game;

import org.minueto.image.MinuetoImage;

import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon.TerrainType;

public class GameBoardImage extends MinuetoImage {
	
	public GameBoardImage(GameBoard board) {
		System.out.println("GameBoard()");
		HexagonImage hex = new HexagonImage(TerrainType.SEA);
		draw(hex, 0, 0);
	}

}
