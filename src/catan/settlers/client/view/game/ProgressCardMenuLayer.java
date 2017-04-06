package catan.settlers.client.view.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.network.server.commands.game.PlayProgressCardCommand;
import catan.settlers.server.model.ProgressCards.ProgressCardType;

public class ProgressCardMenuLayer extends ImageLayer {

	private static final int WIDTH = 550, HEIGHT = 575;
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont big_font = new MinuetoFont("arial", 16, false, false);
	private static final MinuetoFont big_font_bold = new MinuetoFont("arial", 22, true, false);
	private static final MinuetoFont normal_font_bold = new MinuetoFont("arial", 17, true, false);
	private static final MinuetoFont small_font = new MinuetoFont("arial", 14, false, false);

	private static HashMap<Integer, MinuetoRectangle> btnBg = new HashMap<>();

	private int box_x, box_y;
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private MinuetoText description;
	private MinuetoText emptyMsg;
	private Button closeButton;
	private MinuetoRectangle cardShadow;
	private boolean clear;

	public ProgressCardMenuLayer() {
		super();

		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = ClientWindow.WINDOW_HEIGHT / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, new MinuetoColor(249, 249, 249), true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, new MinuetoColor(179, 179, 179), false);
		this.title = new MinuetoText("Progress card", title_font, MinuetoColor.BLACK);
		this.description = new MinuetoText("Click on a progress card below to play it", big_font, MinuetoColor.BLACK);
		this.emptyMsg = new MinuetoText("(no progress cards)", big_font_bold, new MinuetoColor(179, 179, 179));

		this.closeButton = new Button(this, "Close", new MinuetoColor(211, 95, 95), new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				gsm.setShowProgressCardMenu(false);
			}
		});
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doShowProgressCardMenu()) {
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

		ArrayList<ProgressCardType> cards = gsm.getProgressCards();

		if (cards.isEmpty()) {
			draw(emptyMsg, box_x + WIDTH / 2 - emptyMsg.getWidth() / 2, box_y + HEIGHT / 2 - emptyMsg.getHeight() / 2);
		} else {
			for (int i = 0; i < cards.size(); i++) {
				ProgressCardType cardType = cards.get(i);
				y_offset = drawCard(cardType, box_x + 10, y_offset, i);
			}
		}

		y_offset = box_y + HEIGHT - closeButton.getImage().getHeight() - 20;
		draw(closeButton.getImage(), box_x + WIDTH / 2 - closeButton.getImage().getWidth() / 2, y_offset);

		draw(border, box_x, box_y);
	}

	private int drawCard(ProgressCardType cardType, int x, int y, int index) {
		int y_offset = y;

		String cardNameStr = cardType.toString();
		cardNameStr = cardNameStr.toLowerCase();
		cardNameStr = cardNameStr.replace('_', ' ');
		cardNameStr = cardNameStr.substring(0, 1).toUpperCase() + cardNameStr.substring(1);

		String cardDescStr = getCardDescription(cardType);

		MinuetoColor cardColor = new MinuetoColor(222, 170, 135);
		MinuetoColor fontColor = cardColor.darken(0.4);

		MinuetoText cardName = new MinuetoText(cardNameStr, normal_font_bold, fontColor);
		MinuetoText cardDesc = new MinuetoText(cardDescStr, small_font, fontColor);

		int bgWidth = WIDTH - 20;
		int btnHeight = cardName.getHeight() + cardDesc.getHeight() + 20;

		if (cardShadow == null)
			cardShadow = new MinuetoRectangle(bgWidth, btnHeight, new MinuetoColor(200, 200, 200), true);

		if (btnBg.get(index) == null)
			btnBg.put(index, new MinuetoRectangle(bgWidth, btnHeight, cardColor, true));

		MinuetoRectangle background = btnBg.get(index);
		draw(cardShadow, x + 3, y + 3);
		draw(background, x, y);

		draw(cardName, x + 10, y_offset + 10);
		y_offset += cardName.getHeight() + 10;

		draw(cardDesc, x + 10, y_offset);
		y_offset += cardDesc.getHeight() + 10;

		registerClickable(background, new ClickListener() {
			@Override
			public void onClick() {
				System.out.println("Playing " + cardType + "...");
				NetworkManager nm = ClientModel.instance.getNetworkManager();
				nm.sendCommand(new PlayProgressCardCommand(cardType));
			}
		});

		return y_offset + 10;
	}

	private String getCardDescription(ProgressCardType cardType) {
		switch (cardType) {
		case ALCHEMIST:
			break;
		case BISHOP:
			return "Move the robber and get a random card from each player on the robber's new hex";
		case COMMERCIAL_HARBOR:
		case CONSTITUTION:
		case CRANE:
		case DESERTER:
		case DIPLOMAT:
		case ENGINEER:
		case INTRIGUE:
		case INVENTOR:
		case IRRIGATION:
		case MASTER_MERCHANT:
			return "Choose two cards to take from an opponent with more VPs than you";
		case MEDICINE:
		case MERCHANT:
		case MERCHANT_FLEET:
		case MINING:
		case PRINTER:
		case RESOURCE_MONOPOLY:
		case ROAD_BUILDING:
		case SABOTEUR:
		case SMITH:
		case SPY:
		case TRADE_MONOPOLY:
		case WARLORD:
		case WEDDING:
			break;
		}
		return "No description";
	}

}
