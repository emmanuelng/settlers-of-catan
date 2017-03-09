package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.view.ClientWindow;

public class DialogBox extends MinuetoRectangle {

	public DialogBox(int sizex, int sizey, String prompt) {
		super(sizex, sizey, MinuetoColor.BLACK, false);

		MinuetoText promptText = new MinuetoText(prompt, new MinuetoFont("arial", 9, false, false), MinuetoColor.BLACK);
		draw(promptText, 1, 1);

		Button yes = new Button("yes", 0, 0, 20, 15);
		Button cancel = new Button("cancel", 0, 0, 30, 15);
		draw(cancel, 45, 20);
		draw(yes, 15, 20);
		ClientWindow.getInstance().getGameWindow().getMouseHandler().register(yes);
		ClientWindow.getInstance().getGameWindow().getMouseHandler().register(cancel);
	}
}
