package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.ImageFileManager;
import catan.settlers.client.view.game.handlers.Clickable;

public class TradeImage extends MinuetoImage implements Clickable {
	private int relativeX;
	private int relativeY;
	private TradeMenu trade;
	private boolean selected;

	public TradeImage(int relativeX, int relativeY) {
		super(51, 60);

		this.relativeX = relativeX;
		this.relativeY = relativeY;

		ImageFileManager ifm = ClientModel.instance.getImageFileManager();
		draw(ifm.load("images/trade-new.png"), 0, 0);
		draw(new MinuetoText("Trade", new MinuetoFont("arial", 10, false, false), MinuetoColor.BLACK), 0, 50);

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

		if (selected) {
			trade = new TradeMenu();
		} else {
			trade.clear();
			trade = null;
		}
	}

	@Override
	public String getName() {
		return "Trade";
	}

}
