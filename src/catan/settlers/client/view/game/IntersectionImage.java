package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;

import catan.settlers.client.model.ClientModel;
import catan.settlers.server.model.map.Intersection;

public class IntersectionImage extends MinuetoImage implements Clickable {

	private int relativeX;
	private int relativeY;
	private Intersection intersection;
	
	public IntersectionImage(int relativeX, int relativeY,Intersection intersection) {
		super(20, 20);

		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.intersection = intersection;
		
		if(intersection == ClientModel.instance.getCurrentIntersection()){
			drawCircle(MinuetoColor.RED,0 ,0 ,20);
		}else{
			drawCircle(new MinuetoColor(204, 204, 204), 0, 0, 20);
		}
	}
	
	@Override
	public boolean isClicked(int x, int y) {
		return x > relativeX && x < relativeX + getWidth() && y > relativeY + 100 && y < relativeY + 100 + getHeight();
	}

	@Override
	public void onclick() {
		System.out.println("Intersection was clicked!");
		if(intersection != ClientModel.instance.getCurrentIntersection()){
			ClientModel.instance.setCurrentIntersection(intersection);
		}else{
			ClientModel.instance.setCurrentIntersection(null);
		}
	}

	@Override
	public String getName() {
		return "Intersection";
	}
}
