package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;

public class SelectProgressCardTypeLayer extends ImageLayer {

	private static enum Field {
		TRADE, SCIENCE, POLITICS
	};

	private static final int WIDTH = 550, HEIGHT = 300;
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont big_font = new MinuetoFont("arial", 16, false, false);
	private static final MinuetoFont normal_font_bold = new MinuetoFont("arial", 17, true, false);

	private static HashMap<Integer, MinuetoRectangle> btnBg = new HashMap<>();

	private int box_x, box_y;
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private MinuetoText description;
	private MinuetoRectangle btnShadow;
	private boolean clear;

	public SelectProgressCardTypeLayer() {
		super();

		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = ClientWindow.WINDOW_HEIGHT / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, new MinuetoColor(249, 249, 249), true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, new MinuetoColor(179, 179, 179), false);
		this.title = new MinuetoText("Select field", title_font, MinuetoColor.BLACK);
		this.description = new MinuetoText("Select one of the three fields blow", big_font, MinuetoColor.BLACK);
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doShowSelectCardTypeMenu()) {
			if (clear) {
				ClientWindow.getInstance().getGameWindow().clearLayerClickables(this);
				clear();
				btnBg = new HashMap<>();
				clear = false;
			}
			return;
		} else {
			clear = true;
		}

		clear();
		draw(background, box_x, box_y);

		int y_offset = box_y + 20;

		draw(title, box_x + WIDTH / 2 - title.getWidth() / 2, y_offset);
		y_offset += title.getHeight() + 15;

		draw(description, box_x + WIDTH / 2 - description.getWidth() / 2, y_offset);
		y_offset += description.getHeight() + 30;

		int btnIndex = 0;
		for (Field field : Field.values()) {
			y_offset = drawButton(field, box_x + 10, y_offset, btnIndex);
			btnIndex++;
		}

		draw(border, box_x, box_y);
	}

	private int drawButton(Field field, int x, int y, int index) {
		int y_offset = y;

		String fieldNameStr = field.toString();
		fieldNameStr = fieldNameStr.toLowerCase();
		fieldNameStr = fieldNameStr.substring(0, 1).toUpperCase() + fieldNameStr.substring(1);

		MinuetoColor fieldColor = getFieldColor(field);
		MinuetoColor fontColor = fieldColor.darken(0.4);

		MinuetoText fieldName = new MinuetoText(fieldNameStr, normal_font_bold, fontColor);

		int bgWidth = WIDTH - 20;
		int btnHeight = fieldName.getHeight() + 20;

		if (btnShadow == null)
			btnShadow = new MinuetoRectangle(bgWidth, btnHeight, new MinuetoColor(200, 200, 200), true);

		if (btnBg.get(index) == null)
			btnBg.put(index, new MinuetoRectangle(bgWidth, btnHeight, fieldColor, true));

		MinuetoRectangle background = btnBg.get(index);

		draw(btnShadow, x + 3, y + 3);
		draw(background, x, y);
		draw(fieldName, x + bgWidth / 2 - fieldName.getWidth() / 2, y + btnHeight / 2 - fieldName.getHeight() / 2);

		registerClickable(background, new ClickListener() {

			@Override
			public void onClick() {
				// TODO
				System.out.println("You selected " + field);
			}
		});

		y_offset += btnHeight;
		return y_offset + 10;
	}

	private MinuetoColor getFieldColor(Field field) {
		switch (field) {
		case POLITICS:
			return GameWindow.getPoliticsColor();
		case SCIENCE:
			return GameWindow.getScienceColor();
		case TRADE:
			return GameWindow.getTradeColor();
		}

		return MinuetoColor.BLACK;
	}

}
