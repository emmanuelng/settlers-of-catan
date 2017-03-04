package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

import catan.settlers.client.view.game.HexagonImage.HexType;

public class BoardSurface extends MinuetoImage {
	private int[] intersections;
	public BoardSurface(int sizeX,int sizeY){
		super(sizeX,sizeY);
	}
	
	private void drawHex(int posX,int posY,HexType type){
		HexagonImage hex = new HexagonImage(type);
		this.draw(hex.getHexImage(), posX, posY);
		for(int i = 0;i< hex.drawCoordinates(posX, posY).length;i+=2){
			int x = hex.drawCoordinates(posX,posY)[i];
			int y = hex.drawCoordinates(posX,posY)[i+1];
			this.drawIntersection(x,y);
		}
	}
	
	private void drawIntersection(int posX,int posY){
		IntersectionImage intersection = new IntersectionImage();
		this.draw(intersection.getIntersectionImage(), posX-5, posY-5); //5 is half of the intersection's size, so the cneter of intersection is at the vertex
	}
	
	public void drawHexGrid(int x, int y){
		this.drawHex(x, y+90*1, HexType.BRICK);
		this.drawHex(x, y+90*2, HexType.BRICK);
		this.drawHex(x, y+90*3, HexType.BRICK);
		
		this.drawHex(x+80, y-45+90*1, HexType.BRICK);
		this.drawHex(x+80, y-45+90*2, HexType.BRICK);
		this.drawHex(x+80, y-45+90*3, HexType.BRICK);
		this.drawHex(x+80, y-45+90*4, HexType.BRICK);
		
		this.drawHex(x+160, y-90+90*1, HexType.BRICK);
		this.drawHex(x+160, y-90+90*2, HexType.BRICK);
		this.drawHex(x+160, y-90+90*3, HexType.BRICK);
		this.drawHex(x+160, y-90+90*4, HexType.BRICK);
		this.drawHex(x+160, y-90+90*5, HexType.BRICK);
		
		this.drawHex(x+240, y-45+90*1, HexType.BRICK);
		this.drawHex(x+240, y-45+90*2, HexType.BRICK);
		this.drawHex(x+240, y-45+90*3, HexType.BRICK);
		this.drawHex(x+240, y-45+90*4, HexType.BRICK);
		
		this.drawHex(x+320, y+90*1, HexType.BRICK);
		this.drawHex(x+320, y+90*2, HexType.BRICK);
		this.drawHex(x+320, y+90*3, HexType.BRICK);
	}
	
}

