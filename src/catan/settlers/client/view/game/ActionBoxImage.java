package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.view.ClientWindow;

public class ActionBoxImage extends MinuetoImage {

	private static final int WIDTH = 300;
	private static final int PADDING_TOP = 30;
	private static final int PADDING_LEFT = 10;

	private MinuetoRectangle background;
	private MinuetoText title;

	private int box_x, box_y;

	public ActionBoxImage() {
		super(ClientWindow.WINDOW_WIDTH, ClientWindow.WINDOW_HEIGHT);

		this.box_x = ClientWindow.WINDOW_WIDTH - WIDTH;
		this.box_y = 0;

		this.background = new MinuetoRectangle(WIDTH, ClientWindow.WINDOW_HEIGHT, new MinuetoColor(233, 221, 175),
				true);
		this.title = new MinuetoText("ACTIONS", new MinuetoFont("arial", 15, true, false),
				new MinuetoColor(72, 62, 55));

		draw(background, box_x, box_y);
		draw(title, box_x + PADDING_LEFT, box_y + PADDING_TOP);
	}
}
