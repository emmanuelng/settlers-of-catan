package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.Clickable;

public class TradeImage extends MinuetoImage implements Clickable{
	private int relativeX;
	private int relativeY;
	private TradeMenu trade;
	private boolean selected;

	public TradeImage(int relativeX, int relativeY) {
		super(51, 60);

		this.relativeX = relativeX;
		this.relativeY = relativeY;

		MinuetoImage trade;
		try {
			trade = new MinuetoImageFile("images/trade.png");
			draw(trade, 0, 0);
		} catch (MinuetoFileException e) {
			System.out.println("Could not load image file");
			return;
		}
		draw(new MinuetoText("Trade",new MinuetoFont("arial",10,false,false),MinuetoColor.BLACK), 0, 50);
		
		this.selected = false;
	}

	@Override
	public boolean isClicked(int x, int y) {
		// TODO: absolute coordinates are hard coded for now
		return x > relativeX && x < relativeX + getWidth() && y > relativeY && y < relativeY + getHeight();
	}

	@Override
	public void onclick() {
		selected = !selected;

		if(selected){
			trade = new TradeMenu();
		}else{
			trade.clear();
			trade=null;
		}
	}

	@Override
	public String getName() {
		return "Trade";
	}

}
