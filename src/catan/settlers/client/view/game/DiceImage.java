package catan.settlers.client.view.game;

import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;

public class DiceImage extends MinuetoImage implements Clickable {

	private int relativeX;
	private int relativeY;

	public DiceImage(int relativeX, int relativeY) {
		super(51, 51);

		this.relativeX = relativeX;
		this.relativeY = relativeY;

		MinuetoImage dice;
		try {
			dice = new MinuetoImageFile("images/dice.png");
			draw(dice, 0, 0);
		} catch (MinuetoFileException e) {
			System.out.println("Could not load image file");
			return;
		}
	}

	@Override
	public boolean isClicked(int x, int y) {
		// TODO: absolute coordinates are hard coded for now
		return x > relativeX && x < relativeX + getWidth() && y > relativeY && y < relativeY + getHeight();
	}

	@Override
	public void onclick() {
		System.out.println("Dice was clicked!");
	}

	@Override
	public String getName() {
		return "Dice";
	}

}
