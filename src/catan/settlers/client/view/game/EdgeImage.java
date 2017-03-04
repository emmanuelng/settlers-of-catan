package catan.settlers.client.view.game;

import org.minueto.image.MinuetoImage;

public class EdgeImage extends MinuetoImage{
	private MinuetoImage edgeImage;
	private int[] roadCoordinates;
	
	public EdgeImage(int startX,int startY,int endX,int endY){
		edgeImage = new MinuetoImage(RoadLength,RoadWidth);
		edgeImage.drawRectangle();
		roadCoordinates = {startX,startY,endX,endY};
	}
}
