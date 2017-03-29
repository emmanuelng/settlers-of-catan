package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.view.game.handlers.ClickListener;

public class Button {

	private static final MinuetoFont default_btn_font = new MinuetoFont("arial", 16, true, false);

	private MinuetoImage btnImage;

	private ImageLayer parent;
	private ClickListener listener;

	public Button(ImageLayer parent, String text, MinuetoColor color, ClickListener listener) {
		this.parent = parent;
		this.listener = listener;
		
		MinuetoColor textColor = determineTextColor(color);
		MinuetoText textImage = new MinuetoText(text, default_btn_font, textColor);
		MinuetoRectangle background = new MinuetoRectangle(textImage.getWidth() + 30, textImage.getHeight() + 20, color,
				true);

		btnImage = new MinuetoImage(background.getWidth(), background.getHeight());
		btnImage.draw(background, 0, 0);
		btnImage.draw(textImage, background.getWidth() / 2 - textImage.getWidth() / 2,
				background.getHeight() / 2 - textImage.getHeight() / 2);
	}

	public MinuetoImage getImage() {
		parent.registerClickable(btnImage, listener);
		return btnImage;
	}

	private MinuetoColor determineTextColor(MinuetoColor btn_bg_color) {
		int red = btn_bg_color.getRed();
		int green = btn_bg_color.getGreen();
		int blue = btn_bg_color.getBlue();

		/*
		 * Using:
		 * http://stackoverflow.com/questions/3942878/how-to-decide-font-color-
		 * in-white-or-black-depending-on-background-color
		 */
		double value = red * 0.299 + green * 0.587 + blue * 0.114;
		return value > 186 ? MinuetoColor.BLACK : MinuetoColor.WHITE;
	}
}
