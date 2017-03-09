package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

public class Button extends MinuetoRectangle implements Clickable {

	private int relativeX;
	private int relativeY;
	private MinuetoText text;
	private String input;

	public Button(String input, int relativeX, int relativeY, int sizeX, int sizeY) {
		super(sizeX, sizeY, MinuetoColor.BLACK, false);
		text = new MinuetoText(input, new MinuetoFont("arial", 9, false, false), MinuetoColor.BLACK);

		this.input = input;

		draw(text, 1, 1);
		this.relativeX = relativeX;
		this.relativeY = relativeY;

	}

	@Override
	public boolean isClicked(int x, int y) {
		// TODO Auto-generated method stub
		return x > relativeX && x < relativeX + getWidth() && y > relativeY + 100 && y < relativeY + 100 + getHeight();
	}

	@Override
	public void onclick() {
		System.out.println(input + "button was clicked!");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return input;
	}

}
