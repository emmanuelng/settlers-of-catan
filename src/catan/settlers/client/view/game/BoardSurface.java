package catan.settlers.client.view.game;

import org.minueto.image.MinuetoImage;

import catan.settlers.client.view.game.HexagonImage.HexType;

public class BoardSurface extends MinuetoImage {
	public BoardSurface(int sizeX,int sizeY){
		super(sizeX,sizeY);
	}
	
	public void drawHex(int posX,int posY,HexType type){
		HexagonImage hex = new HexagonImage(type);
		this.draw(hex.getHexImage(), posX, posY);

	}
	
	public void drawIntersection(int posX,int posY){
		IntersectionImage intersection = new IntersectionImage();
		this.draw(intersection.getIntersectionImage(), posX, posY);
	}
}
