package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.view.ClientWindow;

public class DialogBox extends MinuetoImage {

	public DialogBox(String title, String message) {
		super(ClientWindow.WINDOW_WIDTH, ClientWindow.WINDOW_HEIGHT);

		MinuetoFont titleFont = new MinuetoFont("arial", 20, true, false);
		MinuetoFont messageFont = new MinuetoFont("arial", 16, false, false);

		MinuetoText titleImage = new MinuetoText(title, titleFont, MinuetoColor.BLACK, true);
		MinuetoText messageImage = new MinuetoText(message, messageFont, MinuetoColor.BLACK, true);

		int backgroundwidth = Math.max(titleImage.getWidth(), messageImage.getWidth()) + 40;
		int backgroundheight = titleImage.getHeight() + messageImage.getHeight() + 20;

		MinuetoRectangle background = new MinuetoRectangle(backgroundwidth, backgroundheight,
				new MinuetoColor(238, 255, 170), true);

		draw(background, getWidth() / 2 - background.getWidth() / 2, 5);
		draw(titleImage, getWidth() / 2 - titleImage.getWidth() / 2, 10);
		draw(messageImage, getWidth() / 2 - messageImage.getWidth() / 2, titleImage.getHeight() + 13);
	}
}
