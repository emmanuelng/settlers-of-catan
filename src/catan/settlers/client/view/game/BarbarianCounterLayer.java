package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.ImageFileManager;

public class BarbarianCounterLayer extends ImageLayer {

	private static final MinuetoFont amt_font = new MinuetoFont("arial", 25, false, false);

	private int barbarianCounter;
	private MinuetoImageFile barbarianImage;

	public BarbarianCounterLayer() {
		super();

		ImageFileManager ifm = ClientModel.instance.getImageFileManager();
		this.barbarianImage = ifm.load("images/barbarian_counter.png");

	}

	@Override
	public void compose(GameStateManager gsm) {

		if (this.barbarianCounter != gsm.getBarbarianCounter()) {
			clear();
		}

		this.barbarianCounter = gsm.getBarbarianCounter();

		MinuetoText counter = new MinuetoText(barbarianCounter + "/7", amt_font, MinuetoColor.BLACK, true);

		draw(barbarianImage, 10, 10);
		draw(counter, barbarianImage.getWidth()+20, barbarianImage.getHeight() - 25);

	}

}
