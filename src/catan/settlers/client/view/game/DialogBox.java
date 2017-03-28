package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.ClientWindow;

public class DialogBox extends ImageLayer {

	private static final MinuetoFont title_font = new MinuetoFont("arial", 20, true, false);
	private static final MinuetoFont message_font = new MinuetoFont("arial", 16, false, false);
	private static final MinuetoColor bg_color = new MinuetoColor(238, 255, 170);

	public DialogBox() {
		super();
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (gsm.getdBoxTitle() == null && gsm.getdBoxMessage() == null)
			return;

		clear();

		String title = gsm.getdBoxTitle();
		String message = gsm.getdBoxMessage();

		MinuetoText titleImage = new MinuetoText(title, title_font, MinuetoColor.BLACK, true);
		MinuetoText messageImage = new MinuetoText(message, message_font, MinuetoColor.BLACK, true);

		int backgroundwidth = Math.max(titleImage.getWidth(), messageImage.getWidth()) + 40;
		int backgroundheight = titleImage.getHeight() + messageImage.getHeight() + 20;

		MinuetoRectangle background = new MinuetoRectangle(backgroundwidth, backgroundheight, bg_color, true);

		draw(background, ClientWindow.WINDOW_WIDTH / 2 - background.getWidth() / 2, 5);
		draw(titleImage, ClientWindow.WINDOW_WIDTH / 2 - titleImage.getWidth() / 2, 10);
		draw(messageImage, ClientWindow.WINDOW_WIDTH / 2 - messageImage.getWidth() / 2, titleImage.getHeight() + 13);

	}
}
