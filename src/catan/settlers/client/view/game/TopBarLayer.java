package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoCircle;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.ImageFileManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.network.server.commands.game.EndTurnCommand;
import catan.settlers.network.server.commands.game.RollDiceCommand;
import catan.settlers.server.model.Player.ResourceType;

public class TopBarLayer extends ImageLayer {

	private static final MinuetoColor shadow_color = new MinuetoColor(236, 236, 236);
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor vp_color = new MinuetoColor(200, 171, 55);
	static final HashMap<ResourceType, MinuetoColor> resourceColors = new HashMap<>();

	private static final MinuetoFont vp_font = new MinuetoFont("arial", 25, true, false);
	private static final MinuetoFont resource_name_font = new MinuetoFont("arial", 17, true, false);
	private static final MinuetoFont resource_amt_font = new MinuetoFont("arial", 17, false, false);

	private HashMap<ResourceType, Integer> resources;

	private MinuetoRectangle shadowImage;
	private MinuetoRectangle bgImage;
	private MinuetoImageFile diceImage;
	private MinuetoImageFile vpImage;
	private MinuetoImageFile endTurnImage;
	private MinuetoImageFile tradeImage;
	private MinuetoImage cardsImage;
	private MinuetoImageFile menuImage;

	public TopBarLayer() {
		super();
		initializeResources();

		this.shadowImage = new MinuetoRectangle(ClientWindow.WINDOW_WIDTH, 105, shadow_color, true);
		this.bgImage = new MinuetoRectangle(ClientWindow.WINDOW_WIDTH, 100, bg_color, true);

		ImageFileManager ifm = ClientModel.instance.getImageFileManager();
		this.diceImage = ifm.load("images/dice-new.png");
		this.vpImage = ifm.load("images/vp-new.png");
		this.endTurnImage = ifm.load("images/endturn-new.png");
		this.tradeImage = ifm.load("images/trade-new.png");
		this.cardsImage = ifm.load("images/cards-new.png");
		this.menuImage = ifm.load("images/menu_player"
				+ ClientWindow.getInstance().getGameWindow().getPlayerNumber(ClientModel.instance.getUsername())
				+ ".png");
		
		registerClickable(menuImage, new ClickListener() {
			@Override
			public void onClick() {
				System.out.println("Clicked!");
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				boolean b = !gsm.getShowSaveMenu();
				gsm.setShowSaveMenu(b);
			}
		});
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doUpdateResources())
			return;

		resources = gsm.getResources();
		gsm.getBarbarianCounter();
		draw(shadowImage, 0, 0);
		draw(bgImage, 0, 0);

		draw(menuImage, 30, 100 / 2 - menuImage.getHeight() / 2);

		int resources_x = menuImage.getWidth() + 70, resources_y = 25, resources_per_line = 5;
		for (int i = 0; i < ResourceType.values().length; i++) {
			ResourceType rtype = ResourceType.values()[i];
			drawResource(rtype, resources_x + 150 * (i % resources_per_line),
					resources_y + 40 * (int) Math.floor(i / resources_per_line));
		}

		int x_offset = ClientWindow.WINDOW_WIDTH - vpImage.getWidth() - 30;

		MinuetoText vpAmt = new MinuetoText("" + gsm.getVictoryPoint(), vp_font, vp_color, true);

		draw(vpImage, x_offset, 0);
		draw(vpAmt, x_offset + 75, 35);
		x_offset -= (endTurnImage.getWidth() + 50);

		draw(endTurnImage, x_offset, 25);
		x_offset -= (tradeImage.getWidth() + 50);

		draw(tradeImage, x_offset, 25);
		x_offset -= (diceImage.getWidth() + 50);

		draw(diceImage, x_offset, 25);
		x_offset -= (cardsImage.getWidth() + 50);

		draw(cardsImage, x_offset, 25);

		setClickables();
	}

	private void drawResource(ResourceType r, int x, int y) {
		String rname = r.toString().toLowerCase();
		rname = rname.substring(0, 1).toUpperCase() + rname.substring(1);
		String amtStr = resources.get(r).toString();

		MinuetoColor color = GameWindow.getColorByResource(r);
		MinuetoCircle circleImage = new MinuetoCircle(10, color, true);
		MinuetoCircle circleBorderImage = new MinuetoCircle(10, color.darken(0.2), false);
		MinuetoText rNameImage = new MinuetoText(rname, resource_name_font, MinuetoColor.BLACK, true);
		MinuetoText rAmtImage = new MinuetoText(amtStr, resource_amt_font, MinuetoColor.BLACK, true);

		draw(circleImage, x, y);
		draw(circleBorderImage, x, y);
		draw(rNameImage, x + circleImage.getWidth() + 10, y);
		draw(rAmtImage, x + circleImage.getWidth() + rNameImage.getWidth() + 15, y);
	}

	private void setClickables() {
		// Dice
		registerClickable(diceImage, new ClickListener() {
			@Override
			public void onClick() {
				ClientModel.instance.getNetworkManager().sendCommand(new RollDiceCommand());
			}
		});

		// End turn
		registerClickable(endTurnImage, new ClickListener() {
			@Override
			public void onClick() {
				ClientModel.instance.getNetworkManager().sendCommand(new EndTurnCommand());

				// Reset the client model
				ClientModel.instance.getGameStateManager().setSelectedEdge(null);
				ClientModel.instance.getGameStateManager().setSelectedIntersection(null);
				ClientModel.instance.getGameStateManager().setdBox(null, null);
				ClientModel.instance.getGameStateManager().setMerchantFleetAdvantage(null);
				ClientModel.instance.getGameStateManager().setMerchantFleetAdvantage(null);
			}
		});

		// Trade
		registerClickable(tradeImage, new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				boolean showWindow = !gsm.doShowTradeMenu();
				gsm.setShowTradeMenu(showWindow);
			}
		});

		// Cards
		registerClickable(cardsImage, new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				boolean showWindow = !gsm.doShowProgressCardMenu();
				gsm.setShowProgressCardMenu(showWindow);
			}
		});
	}

	private void initializeResources() {
		this.resources = ClientModel.instance.getGameStateManager().getResources();
		if (this.resources == null) {
			this.resources = new HashMap<>();
			for (ResourceType r : ResourceType.values()) {
				this.resources.put(r, 0);
			}
		}
	}
}
