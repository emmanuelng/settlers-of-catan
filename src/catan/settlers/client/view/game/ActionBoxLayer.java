package catan.settlers.client.view.game;

import java.util.ArrayList;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.actions.GameAction;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.server.model.Game.GamePhase;

public class ActionBoxLayer extends ImageLayer {

	private static final int WIDTH = 330;
	private static final int PADDING_TOP = 30;
	private static final int PADDING_LEFT = 10;

	private static final MinuetoColor BACKGROUND_COLOR = new MinuetoColor(233, 221, 175);
	private static final MinuetoColor TITLE_COLOR = new MinuetoColor(172, 167, 147);
	private static final MinuetoColor BUTTON_COLOR = new MinuetoColor(244, 238, 215);
	private static final MinuetoColor SHADOW_COLOR = new MinuetoColor(222, 205, 135);

	private MinuetoRectangle background;
	private MinuetoRectangle background_shadow;
	private MinuetoText title;
	private int box_x, box_y;

	public ActionBoxLayer() {
		super();

		this.box_x = ClientWindow.WINDOW_WIDTH - WIDTH;
		this.box_y = 0;

		this.background = new MinuetoRectangle(WIDTH, ClientWindow.WINDOW_HEIGHT, BACKGROUND_COLOR, true);
		this.background_shadow = new MinuetoRectangle(WIDTH, ClientWindow.WINDOW_HEIGHT, SHADOW_COLOR, true);
		this.title = new MinuetoText("ACTIONS", new MinuetoFont("arial", 15, true, false), TITLE_COLOR);
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doUpdateActions() || gsm.getCurrentPhase() != GamePhase.TURNPHASE)
			return;

		if (!gsm.getCurrentPlayer().equals(ClientModel.instance.getUsername()))
			return;

		ClientWindow.getInstance().getGameWindow().clearLayerClickables(this);
		clear();

		draw(background_shadow, box_x - 3, box_y);
		draw(background, box_x, box_y);

		int y_offset = box_y + PADDING_TOP;
		int x_offset = box_x + PADDING_LEFT;

		draw(title, x_offset, y_offset);
		y_offset += title.getHeight() + 20;

		if (gsm.isMoveKnightMode()) {
			moveKnightMode(x_offset, y_offset);
		} else {
			normalMode(x_offset, y_offset, gsm);
		}
	}

	private void normalMode(int x_offset, int y_offset, GameStateManager gsm) {
		// Trade actions
		int tradeLevel = gsm.getTradeImprovementLevel();
		MinuetoText tradeTitle = new MinuetoText("Trade (level " + tradeLevel + ")",
				new MinuetoFont("arial", 15, true, false), TITLE_COLOR);
		draw(tradeTitle, x_offset, y_offset);
		y_offset += tradeTitle.getHeight() + 10;
		y_offset = drawActions(x_offset, y_offset, ClientModel.instance.getActionManager().getTradeActions());

		// Politics actions
		int politicsLevel = gsm.getPoliticsImprovementLevel();
		MinuetoText politicsTitle = new MinuetoText("Politics (level " + politicsLevel + ")",
				new MinuetoFont("arial", 15, true, false), TITLE_COLOR);
		draw(politicsTitle, x_offset, y_offset);
		y_offset += politicsTitle.getHeight() + 10;
		y_offset = drawActions(x_offset, y_offset, ClientModel.instance.getActionManager().getPoliticActions());

		// Science actions
		int scienceLevel = gsm.getScienceImprovementLevel();
		MinuetoText scienceTitle = new MinuetoText("Science (level " + scienceLevel + ")",
				new MinuetoFont("arial", 15, true, false), TITLE_COLOR);
		draw(scienceTitle, x_offset, y_offset);
		y_offset += scienceTitle.getHeight() + 10;
		y_offset = drawActions(x_offset, y_offset, ClientModel.instance.getActionManager().getScienceActions());

		// Miscellaneous actions
		MinuetoText miscTitle = new MinuetoText("Miscellaneous", new MinuetoFont("arial", 15, true, false),
				TITLE_COLOR);
		draw(miscTitle, x_offset, y_offset);
		y_offset += miscTitle.getHeight() + 10;
		y_offset = drawActions(x_offset, y_offset, ClientModel.instance.getActionManager().getMiscActions());

	}

	private void moveKnightMode(int x_offset, int y_offset) {
		// Trade actions
		MinuetoText moveKnightTitle = new MinuetoText("Move knight", new MinuetoFont("arial", 15, true, false),
				TITLE_COLOR);
		draw(moveKnightTitle, x_offset, y_offset);
		y_offset += moveKnightTitle.getHeight() + 10;
		y_offset = drawActions(x_offset, y_offset, ClientModel.instance.getActionManager().getMoveKnightActions());
	}

	private int drawActions(int x, int y, ArrayList<GameAction> arrayList) {
		int y_offset = y;

		for (GameAction action : arrayList) {
			y_offset += addActionButton(action, x, y_offset) + 5;
		}

		return y_offset + 10;
	}

	private int addActionButton(GameAction action, int x, int y) {
		// Setup the button's text
		MinuetoFont titleFont = new MinuetoFont("arial", 14, false, false);
		MinuetoText title = new MinuetoText(action.getDescription(), titleFont, MinuetoColor.BLACK);

		MinuetoFont msgFont = new MinuetoFont("arial", 12, false, false);
		String msg = action.isPossible() ? action.getSuccessMessage() : action.getFailureMessage();
		MinuetoColor msgColor = action.isPossible() ? new MinuetoColor(33, 120, 68) : MinuetoColor.RED;
		MinuetoText msgImage = new MinuetoText(msg, msgFont, msgColor);

		// Compute the button's size
		int btn_width = WIDTH - 2 * PADDING_LEFT;
		int btn_height = title.getHeight() + msgImage.getHeight() + 15;
		MinuetoRectangle btnBackground = new MinuetoRectangle(btn_width, btn_height, BUTTON_COLOR, true);
		MinuetoRectangle btnShadow = new MinuetoRectangle(btn_width, btn_height, SHADOW_COLOR, true);

		// Compute the text position
		int desc_x = x + 15;
		int desc_y = y + btn_height / 2 - title.getHeight();

		// Draw the button
		draw(btnShadow, x + 3, y + 3);
		draw(btnBackground, x, y);
		draw(title, desc_x, desc_y);
		draw(msgImage, desc_x, desc_y + title.getHeight() + 2);

		// Register the button
		registerClickable(btnBackground, new ClickListener() {
			@Override
			public void onClick() {
				if (action.isPossible())
					action.perform();
			}
		});

		return btn_height;
	}
}
