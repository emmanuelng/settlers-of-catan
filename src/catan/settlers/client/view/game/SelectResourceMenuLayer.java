package catan.settlers.client.view.game;

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
import catan.settlers.network.server.commands.game.ResourceSelectedCommand;
import catan.settlers.server.model.Player.ResourceType;

public class SelectResourceMenuLayer extends ImageLayer {

	private static final int WIDTH = 500, HEIGHT = 450;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont description_font_bold = new MinuetoFont("arial", 17, true, false);
	private static final MinuetoFont description_font = new MinuetoFont("arial", 16, false, false);

	private static HashMap<ResourceType, MinuetoRectangle> btn_bg = new HashMap<>();

	private int box_x, box_y;
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private boolean clear;
	private MinuetoRectangle btnShadow;

	public SelectResourceMenuLayer() {
		super();

		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = (ClientWindow.WINDOW_HEIGHT + 100) / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);
		this.title = new MinuetoText("Select a resource", title_font, MinuetoColor.BLACK);

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

		if (!gsm.getSelectResourceMessage().isEmpty()) {
			MinuetoText message = new MinuetoText(gsm.getSelectResourceMessage(), description_font, MinuetoColor.BLACK);
			draw(message, box_x + (WIDTH / 2 - message.getWidth() / 2), y_offset);
			y_offset += 40;
		} else {
			y_offset += 20;
		}

		drawResourceBoxes(box_x + 10, y_offset, gsm);
	}

	private void drawResourceBoxes(int x, int y, GameStateManager gsm) {
		int y_offset = y;
		String menuReason = ClientModel.instance.getGameStateManager().getShowSelectResourceMenuReason();

		if (menuReason == null) {
			for (ResourceType rtype : ResourceType.values()) {
				// Ignore commodities
				if (rtype == ResourceType.COIN || rtype == ResourceType.CLOTH || rtype == ResourceType.PAPER)
					continue;

				if (gsm.getResources().get(rtype) < 1)
					continue;

				String rnameStr = rtype.toString().toLowerCase();
				rnameStr = rnameStr.substring(0, 1).toUpperCase() + rnameStr.substring(1);
				MinuetoColor resourceColor = GameWindow.getColorByResource(rtype);

				MinuetoText rname = new MinuetoText(rnameStr, description_font_bold, resourceColor.darken(0.3));

				if (btn_bg.get(rtype) == null)
					btn_bg.put(rtype, new MinuetoRectangle(WIDTH - 30, rname.getHeight() + 30, resourceColor, true));

				if (btnShadow == null)
					btnShadow = new MinuetoRectangle(WIDTH - 30, rname.getHeight() + 30, bg_color.darken(0.2), true);

				MinuetoRectangle background = btn_bg.get(rtype);

				draw(btnShadow, x + 3, y_offset + 3);
				draw(background, x, y_offset);
				draw(rname, x + background.getWidth() / 2 - rname.getWidth() / 2,
						y_offset + background.getHeight() / 2 - rname.getHeight() / 2);

				registerClickable(background, new ClickListener() {
					@Override
					public void onClick() {
						System.out.println("Selected " + rtype);
						NetworkManager nm = ClientModel.instance.getNetworkManager();
						nm.sendCommand(new ResourceSelectedCommand(rtype));
					}
				});

				y_offset += background.getHeight() + 10;
			}
		} else if(menuReason == "Aqueduct"){
			for (ResourceType rtype : ResourceType.values()) {
				// Ignore commodities
				String rnameStr = rtype.toString().toLowerCase();
				rnameStr = rnameStr.substring(0, 1).toUpperCase() + rnameStr.substring(1);
				MinuetoColor resourceColor = GameWindow.getColorByResource(rtype);

				MinuetoText rname = new MinuetoText(rnameStr, description_font_bold, resourceColor.darken(0.3));

				if (btn_bg.get(rtype) == null)
					btn_bg.put(rtype, new MinuetoRectangle(WIDTH - 30, rname.getHeight() + 30, resourceColor, true));

				if (btnShadow == null)
					btnShadow = new MinuetoRectangle(WIDTH - 30, rname.getHeight() + 30, bg_color.darken(0.2), true);

				MinuetoRectangle background = btn_bg.get(rtype);

				draw(btnShadow, x + 3, y_offset + 3);
				draw(background, x, y_offset);
				draw(rname, x + background.getWidth() / 2 - rname.getWidth() / 2,
						y_offset + background.getHeight() / 2 - rname.getHeight() / 2);

				registerClickable(background, new ClickListener() {
					@Override
					public void onClick() {
						System.out.println("Selected " + rtype);
						NetworkManager nm = ClientModel.instance.getNetworkManager();
						nm.sendCommand(new ResourceSelectedCommand(rtype));
					}
				});

				y_offset += background.getHeight() + 10;
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
