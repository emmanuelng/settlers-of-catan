package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;

public class DialogBox extends MinuetoImage {

	public DialogBox(String title, String message) {
		super(ClientModel.WINDOW_WIDTH, ClientModel.WINDOW_HEIGHT);

		MinuetoFont titleFont = new MinuetoFont("arial", 20, true, false);
		MinuetoFont messageFont = new MinuetoFont("arial", 16, false, false);

		MinuetoText titleImage = new MinuetoText(title, titleFont, MinuetoColor.WHITE, true);
		MinuetoText messageImage = new MinuetoText(message, messageFont, MinuetoColor.WHITE, true);

		int backgroundwidth = Math.max(titleImage.getWidth(), messageImage.getWidth()) + 40;
		int backgroundheight = titleImage.getHeight() + messageImage.getHeight() + 20;

		MinuetoRectangle background = new MinuetoRectangle(backgroundwidth, backgroundheight,
				new MinuetoColor(102, 102, 102), true);

		draw(background, getWidth() / 2 - background.getWidth() / 2, 5);
		draw(titleImage, getWidth() / 2 - titleImage.getWidth() / 2, 10);
		draw(messageImage, getWidth() / 2 - messageImage.getWidth() / 2, titleImage.getHeight() + 13);
	}
}
