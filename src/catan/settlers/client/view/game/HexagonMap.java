package catan.settlers.client.view.game;
import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

import catan.settlers.client.view.game.HexagonImage.HexType;

public class HexagonMap {
	
	public MinuetoImage DrawHexagonMap(int posX, int posY){
		MinuetoImage map = new MinuetoImage(SCRSIZE,SCRSIZE);
		
		HexagonImage.setHeight(HEXSIZE);
		HexagonImage.setSide(HEXSIZE);
		HexagonImage.setBorders(BORDERS);
		
		HexagonImage x = new HexagonImage(HexType.WHEAT);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX,posY+90*1), MinuetoColor.YELLOW);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX,posY+90*2), MinuetoColor.YELLOW);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX,posY+90*3), MinuetoColor.YELLOW);
		
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+80,posY-45+90*1), MinuetoColor.GREEN);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+80,posY-45+90*2), MinuetoColor.GREEN);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+80,posY-45+90*3), MinuetoColor.GREEN);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+80,posY-45+90*4), MinuetoColor.GREEN);
		
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*1), MinuetoColor.BLUE);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*2), MinuetoColor.BLUE);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*3), MinuetoColor.BLUE);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*4), MinuetoColor.BLUE);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*5), MinuetoColor.BLUE);
		
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+240,posY-45+90*1), MinuetoColor.BLACK);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+240,posY-45+90*2), MinuetoColor.BLACK);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+240,posY-45+90*3), MinuetoColor.BLACK);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+240,posY-45+90*4), MinuetoColor.BLACK);
		
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+320,posY+90*1), MinuetoColor.RED);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+320,posY+90*2), MinuetoColor.RED);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+320,posY+90*3), MinuetoColor.RED);

		return map;
	}

	public static MinuetoImage drawHex(MinuetoImage x, int[] coordinates, MinuetoColor color){
		x.drawPolygon(color, coordinates);
		return x;
	}
	

	final static int BORDERS = 25;
	final static int HEXSIZE = 50;
	final static int BSIZE = 19; //changes size of the board
	final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS *3;
	final static int[][] board = new int[BSIZE][BSIZE];
}
