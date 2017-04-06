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
import catan.settlers.network.server.commands.game.SevenDiscardCommand;
import catan.settlers.network.server.commands.game.cards.SelectResourceResponseCommand;
import catan.settlers.server.model.Player.ResourceType;

public class SelectResourceMenuLayer extends ImageLayer {

	private static final int WIDTH = 1000, HEIGHT = 625;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont description_font_bold = new MinuetoFont("arial", 17, true, false);
	private static final MinuetoColor confirm_btn_color = new MinuetoColor(55, 200, 113);

	private int box_x;
	private int box_y;

	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoRectangle rBox;
	private MinuetoRectangle rBoxBorder;
	private MinuetoText title;
	private int resourcesToShow = ResourceType.values().length;

	private boolean clear;

	public SelectResourceMenuLayer() {
		super();
		// modify title based on reason
		String reason = ClientModel.instance.getGameStateManager().getShowSelectResourceMenuReason();
		String title = "Somebody played " + reason;

		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = (ClientWindow.WINDOW_HEIGHT + 100) / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);
		this.title = new MinuetoText(title, title_font, MinuetoColor.BLACK);

	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doShowSelectResourceMenu()) {
			if (clear) {
				ClientWindow.getInstance().getGameWindow().clearLayerClickables(this);
				clear();
				clear = false;
			}
			return;
		} else {
			clear = true;
		}

		draw(background, box_x, box_y);
		draw(border, box_x, box_y);
		overrideClickables();

		int y_offset = box_y + 20;

		draw(title, box_x + (WIDTH / 2 - title.getWidth() / 2), y_offset);
		y_offset += title.getHeight() + 10;

		y_offset += 140;

		drawResourceBoxes(box_x + 10, y_offset);
	}

	private void drawResourceBoxes(int x, int y) {
		int spacing = (WIDTH - 40) / ResourceType.values().length;

		rBox = new MinuetoRectangle(spacing - 30, 50, MinuetoColor.WHITE, true);
		rBoxBorder = new MinuetoRectangle(spacing - 30, 50, border_color, false);
		String menuReason = ClientModel.instance.getGameStateManager().getShowSelectResourceMenuReason();
		if (menuReason == null) {
			for (int i = 0; i < resourcesToShow; i++) {
				ResourceType rType = ResourceType.values()[i];
				String rname = rType.toString().toLowerCase();
				rname = rname.substring(0, 1).toUpperCase() + rname.substring(1);

				MinuetoText rnameImage = new MinuetoText(rname, description_font_bold, MinuetoColor.BLACK);

				int y_offset = y;

				int res_box_x = (x + i * spacing) + (spacing / 2 - rBox.getWidth() / 2);
				
				draw(rBox, res_box_x, y_offset);

				draw(rBoxBorder, res_box_x, y_offset);


				draw(rnameImage, (x + i * spacing) + (spacing / 2 - rnameImage.getWidth() / 2), y_offset+ rBox.getHeight()/2);
				registerClickable(rnameImage, new ClickListener() {
					@Override
					public void onClick() {
						ClientModel.instance.getNetworkManager().sendCommand(new SelectResourceResponseCommand(rType));
						ClientModel.instance.getGameStateManager().setShowSelectResourceMenu(false);
						
						System.out.println(rType);
					}
				});
			}
		}

	}


	private void overrideClickables() {
		/*
		 * Add an dummy clickables to override the clickables on the background
		 * (e.g we should not be able to select an intersection behind the trade
		 * menu)
		 */

		registerClickable(background, new ClickListener() {
			@Override
			public void onClick() {
				// Do nothing
			}
		});
	}

}
