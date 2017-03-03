	package catan.settlers.client.model;

import java.awt.image.BufferedImage;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

import catan.settlers.client.view.GameFrame;

public class IntersectionImage extends MinuetoImage{
	
	private int SQR_SIZE = 10;
	private MinuetoImage intersectionImage;
	private boolean occupied;
	
	public IntersectionImage(){
		intersectionImage = new MinuetoImage(SQR_SIZE,SQR_SIZE);
		intersectionImage.drawRectangle(MinuetoColor.BLACK, 0, 0, SQR_SIZE, SQR_SIZE);
	}
	
	public void setOccupied(){
		this.occupied = true;
	}
	
	public MinuetoImage getIntersectionImage(){
		return intersectionImage;
	}
}
