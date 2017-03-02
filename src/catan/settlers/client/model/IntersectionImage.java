package catan.settlers.client.model;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

import catan.settlers.client.view.GameFrame;

public class IntersectionImage extends MinuetoImage{
	
	private int SQR_SIZE = 10;
	private MinuetoImage intersectionImage;
	
	public IntersectionImage(){
		
	}
	
	public void drawIntersection(int posX,int posY,boolean occupied){
		if(!occupied){
			intersectionImage = new MinuetoImage(SQR_SIZE,SQR_SIZE);
			intersectionImage.drawRectangle(MinuetoColor.BLACK,posX,posY,SQR_SIZE,SQR_SIZE);
			//GameFrame.getInstance().draw(intersectionImage, posX, posY);
		}
	}
}
