package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoCircle;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoText;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.server.model.Player.ResourceType;

public class ResourceBarImage extends MinuetoImage{
	public ResourceBarImage(){
		super(ClientWindow.getInstance().getGameWindow().getWidth(),100);
		clear(new MinuetoColor(255,165,0));
		compose(0,0);
	}
	
	private void compose(int x, int y){
		drawResource(ResourceType.GRAIN, x+ 100, 0);
		drawResource(ResourceType.LUMBER, x+ 250, 0);
		drawResource(ResourceType.ORE, x+ 400, 0);
		drawResource(ResourceType.WOOL, x+ 550, 0);
		drawResource(ResourceType.BRICK, x+ 700, 0);
		drawResource(ResourceType.CLOTH, x+ 100, 50);
		drawResource(ResourceType.PAPER, x+ 250, 50);
		drawResource(ResourceType.COIN, x+ 400, 50);
		//drawResource(ResourceType.GOLDCOIN, x+ 100, 0);
		
		drawDice(x+ 900, 25);
	}
	
	private void drawResource(ResourceType r, int x, int y){
		MinuetoCircle resourceCircle = new MinuetoCircle(20,MinuetoColor.BLACK,true);
		MinuetoFont usedFont = new MinuetoFont("arial", 20, false, false);
		MinuetoText numberOfResource = new MinuetoText("0",usedFont,MinuetoColor.WHITE);
		MinuetoText typeOfResource = new MinuetoText("" + r,usedFont,MinuetoColor.BLACK);
		draw(resourceCircle,x,y);
		draw(numberOfResource,x+15,y+10);
		draw(typeOfResource, x+ 50,y+10);
	}
	
	private void drawDice(int x, int y) {
		MinuetoImage dice;
		try {
			dice = new MinuetoImageFile("images/dice.png");
		} catch (MinuetoFileException e) {
			System.out.println("Could not load image file");
			return;
		}
		this.draw(dice, x, y);
	}
	
}
