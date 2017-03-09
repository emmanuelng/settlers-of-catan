package catan.settlers.client.view.game;

import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;

import catan.settlers.client.model.ClientModel;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Village;

public class VillageImage extends MinuetoImage implements Clickable{
	
	private int relativeX;
	private int relativeY;
	private Intersection intersection;
	private IntersectionUnit intUnit;
	
	public VillageImage(int relativeX, int relativeY, Intersection i){
		super();
		this.relativeX=relativeX;
		this.relativeY=relativeY;
		this.intersection=i;
		
		MinuetoImage settlement;
		try{
			settlement = new MinuetoImageFile("images/building.png");
			draw(settlement,0,0);
		}catch(MinuetoFileException e){
			System.out.println("couldnt load");
			return;
		}
	}

	@Override
	public boolean isClicked(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onclick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
