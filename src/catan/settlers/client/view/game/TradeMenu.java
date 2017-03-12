package catan.settlers.client.view.game;

import java.util.ArrayList;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoCircle;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.network.client.Client;
import catan.settlers.server.model.Player.ResourceType;

public class TradeMenu extends MinuetoImage{
	
	private ArrayList<String> offer;
	private MinuetoImage surface;
	
	public TradeMenu(){
		super(ClientModel.WINDOW_WIDTH,ClientModel.WINDOW_HEIGHT);
		this.clear(MinuetoColor.BLACK.lighten(100));
		
		//display the trade
		drawResource(ResourceType.GRAIN, 100, 25);
		drawResource(ResourceType.LUMBER, 100, 100);
		drawResource(ResourceType.ORE, 100, 175);
		drawResource(ResourceType.WOOL, 100, 250);
		drawResource(ResourceType.BRICK, 100, 325);
		drawResource(ResourceType.CLOTH, 100, 400);
		drawResource(ResourceType.PAPER, 100, 475);
		drawResource(ResourceType.COIN, 100, 550);
		
		offer = new ArrayList<String>();
		
		ClientWindow.getInstance().getGameWindow().setTradeMenu(this);
	}
	
	private void drawResource(ResourceType r, int x, int y) {

		MinuetoColor color;

		switch (r) {
		case GRAIN:
			color = new MinuetoColor(198, 233, 175);
			break;
		case LUMBER:
			color = new MinuetoColor(222, 170, 135);
			break;
		case ORE:
			color = new MinuetoColor(200, 190, 183);
			break;
		case WOOL:
			color = new MinuetoColor(255, 246, 213);
			break;
		case BRICK:
			color = new MinuetoColor(255, 153, 85);
			break;
		case CLOTH:
			color = new MinuetoColor(255, 230, 213);
			break;
		case PAPER:
			color = new MinuetoColor(246, 255, 213);
			break;
		default:
			color = new MinuetoColor(198, 233, 175);
			break;
		}

		MinuetoCircle resourceCircle = new MinuetoCircle(20, color, true);
		MinuetoFont usedFont = new MinuetoFont("arial", 20, false, false);
		MinuetoText numberOfResource = new MinuetoText("", usedFont, MinuetoColor.BLACK);
		MinuetoText typeOfResource = new MinuetoText("" + r, usedFont, MinuetoColor.BLACK);
		ClickableText plus = new ClickableText(x+200,y+100,""+ r,"+",new MinuetoFont("arial", 40, false, false),MinuetoColor.BLACK);
		ClickableText minus = new ClickableText(x,y+100,""+ r,"-",new MinuetoFont("arial", 40, false, false),MinuetoColor.BLACK);
		draw(resourceCircle, x+40, y);
		draw(numberOfResource, x + 55, y + 10);
		draw(typeOfResource, x + 90, y + 10);
		draw(minus, x, y);
		draw(plus, x + 200, y);
		ClientWindow.getInstance().getGameWindow().getMouseHandler().register(plus);
		ClientWindow.getInstance().getGameWindow().getMouseHandler().register(minus);

	}

	public void updateTradeMenu(String resourceType, String text) {
		surface = new MinuetoImage(ClientWindow.getInstance().getGameWindow().getWidth(),ClientWindow.getInstance().getGameWindow().getHeight());
		
		offer.add(resourceType);
		MinuetoFont usedFont = new MinuetoFont("arial", 20, false, false);
		MinuetoText offerResource = new MinuetoText("Your offer",usedFont, MinuetoColor.BLACK);
		
		for(int i=0;i<offer.size();i++){
			MinuetoText typeOfResource = new MinuetoText("1 " + offer.get(i), usedFont, MinuetoColor.BLACK);
			surface.draw(typeOfResource,450,200+i*50);
		}
		
		surface.draw(offerResource,400,150);
		draw(surface,0,0);
	}
	
}


