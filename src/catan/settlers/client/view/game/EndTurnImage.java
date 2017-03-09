package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoText;

import catan.settlers.client.view.game.handlers.Clickable;

public class EndTurnImage extends MinuetoImage implements Clickable {
	private int relativeX;
	private int relativeY;

	public EndTurnImage(int relativeX, int relativeY) {
		super(51, 60);

		this.relativeX = relativeX;
		this.relativeY = relativeY;

		MinuetoImage endturn;
		try {
			endturn = new MinuetoImageFile("images/endturn.png");
			draw(endturn, 0, 0);
		} catch (MinuetoFileException e) {
			System.out.println("Could not load image file");
			return;
		}
		draw(new MinuetoText("End Turn", new MinuetoFont("arial", 10, false, false), MinuetoColor.BLACK), 0, 50);
	}

	@Override
	public boolean isClicked(int x, int y) {
		// TODO: absolute coordinates are hard coded for now
		return x > relativeX && x < relativeX + getWidth() && y > relativeY && y < relativeY + getHeight();
	}

	@Override
	public void onclick() {
		System.out.println("End Turn was clicked!");
	}

	@Override
	public String getName() {
		return "End Turn";
	}

}
