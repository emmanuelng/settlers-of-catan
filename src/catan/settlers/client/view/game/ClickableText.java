package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoText;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.Clickable;

public class ClickableText extends MinuetoText implements Clickable {

	private int relativeX;
	private int relativeY;
	private String resourceType;
	private String text;
	private String name;
	
	public ClickableText(int relativeX, int relativeY, String resourceType, String text, MinuetoFont font, MinuetoColor color) {
		super(text, font, color);
		this.relativeX=relativeX;
		this.relativeY=relativeY;
		this.text=text;
		this.resourceType=resourceType;
		this.name= text+ resourceType + "trade";
	}

	@Override
	public boolean isClicked(int x, int y) {
		// TODO Auto-generated method stub
		return x > relativeX && x < relativeX + getWidth() && y > relativeY && y < relativeY + getHeight();
	}

	@Override
	public void onclick() {
		System.out.println(name);
		if(text == "+" || text == "-"){
			ClientWindow.getInstance().getGameWindow().getTradeMenu().updateTradeMenu(resourceType,text);
		}else if(text == "confirm offer"){
			ClientWindow.getInstance().getGameWindow().getTradeMenu().confirmTradeOffer();

		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
