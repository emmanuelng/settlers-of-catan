package catan.settlers.client.model;
import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

public class HexagonMap {
	public HexagonMap(){
		
	}
	
	public MinuetoImage DrawHexagonMap(int posX, int posY){
		MinuetoImage map = new MinuetoImage(SCRSIZE,SCRSIZE);
		
		Hexagon.setHeight(HEXSIZE);
		Hexagon.setSide(HEXSIZE);
		Hexagon.setBorders(BORDERS);
		
		Hexagon x = new Hexagon();
		MinuetoColor color = MinuetoColor.WHITE;
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX,posY+90*1), color.YELLOW);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX,posY+90*2), color.YELLOW);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX,posY+90*3), color.YELLOW);
		
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+80,posY-45+90*1), color.GREEN);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+80,posY-45+90*2), color.GREEN);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+80,posY-45+90*3), color.GREEN);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+80,posY-45+90*4), color.GREEN);
		
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*1), color.BLUE);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*2), color.BLUE);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*3), color.BLUE);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*4), color.BLUE);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+160,posY-90+90*5), color.BLUE);
		
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+240,posY-45+90*1), color.BLACK);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+240,posY-45+90*2), color.BLACK);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+240,posY-45+90*3), color.BLACK);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+240,posY-45+90*4), color.BLACK);
		
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+320,posY+90*1), color.RED);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+320,posY+90*2), color.RED);
		map = HexagonMap.drawHex(map, x.drawCoordinates(posX+320,posY+90*3), color.RED);
		/*Hexagon hex = new Hexagon();
		hex.setColor(MinuetoColor.RED);
		drawHex(map,hex,posX,posY);*/
		return map;
	}
	
	/*public static MinuetoImage drawHex(MinuetoImage drawOn, Hexagon hex, int posX, int posY){
		drawOn.drawPolygon(hex.getColor(), hex.drawCoordinates(posX, posY));
		return drawOn;
	}*/ //should use this one

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
