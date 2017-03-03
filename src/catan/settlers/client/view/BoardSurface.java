package catan.settlers.client.view;

import org.minueto.image.MinuetoImage;

import catan.settlers.client.model.HexagonImage;
import catan.settlers.client.model.HexagonImage.HexType;
import catan.settlers.client.model.IntersectionImage;

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
