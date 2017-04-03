package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.ImageFileManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.network.server.commands.game.PlayerTradeRequestCommand;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;

public class TradeReceivedMenuLayer extends ImageLayer {

	private static final HashMap<ResourceType, MinuetoImage> plusButtons_offer = new HashMap<>();
	private static final HashMap<ResourceType, MinuetoImage> minusButtons_offer = new HashMap<>();
	private static final HashMap<ResourceType, MinuetoImage> plusButtons_price = new HashMap<>();
	private static final HashMap<ResourceType, MinuetoImage> minusButtons_price = new HashMap<>();
	
	private static final int WIDTH = 1000, HEIGHT = 635;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	private static final MinuetoColor counter_propose_btn_color = new MinuetoColor(55, 200, 113);
	private static final MinuetoColor trade_confirm_btn_color = new MinuetoColor(255, 153, 85);
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont description_font = new MinuetoFont("arial", 17, false, false);
	private static final MinuetoFont description_font_bold = new MinuetoFont("arial", 17, true, false);

	
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private MinuetoText description;
	private MinuetoText giveDesc;
	private MinuetoText receiveDesc;
	private Button counterProposeButton;
	private MinuetoRectangle rAmtBox;
	private MinuetoRectangle rAmtBoxBorder;
	private Button tradeConfirmButton;
	private MinuetoText orText;

	private boolean clear;
	private int box_x, box_y;
	private HashMap<ResourceType, Integer> give, get;
	private Player player;
	
	public TradeReceivedMenuLayer(){
		super();
		
		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = (ClientWindow.WINDOW_HEIGHT + 100) / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);
		this.title = new MinuetoText("Trade", title_font, MinuetoColor.BLACK);
	
		this.giveDesc = new MinuetoText("He/She proposed:", description_font_bold, MinuetoColor.BLACK);
		this.receiveDesc = new MinuetoText("In exchange for:", description_font_bold, MinuetoColor.BLACK);
		this.counterProposeButton = new Button(this, "Counter Propose", counter_propose_btn_color, getCounterProposeListener());
		this.tradeConfirmButton = new Button(this, "Accept Offer", trade_confirm_btn_color,
				getTradeConfirmListener());
		this.orText = new MinuetoText("or", description_font, MinuetoColor.BLACK);
	}
	
	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doShowTradeReceivedMenu()) {
			if (clear) {
				give = resetResourceMap();
				get = resetResourceMap();
				ClientWindow.getInstance().getGameWindow().clearLayerClickables(this);
				clear();
				
				clear = false;
			}
			return;
		} else {
			clear = true;
		}

		give = gsm.tradeOfferReceivedWhatYouGive();
		get = gsm.tradeOfferReceivedWhatYouGet();
		player = gsm.getProposedPlayer();
		draw(background, box_x, box_y);
		draw(border, box_x, box_y);
		overrideClickables();

		int y_offset = box_y + 20;

		draw(title, box_x + (WIDTH / 2 - title.getWidth() / 2), y_offset);
		y_offset += title.getHeight() + 10;

		this.description = new MinuetoText("You got a trade offer sent by " + player.getUsername() , description_font,
				MinuetoColor.BLACK);
		
		draw(description, box_x + (WIDTH / 2 - description.getWidth() / 2), y_offset);
		y_offset += description.getHeight() + 20;

		draw(giveDesc, box_x + 20, y_offset);
		y_offset += giveDesc.getHeight() + 10;

		drawResourceBoxes(box_x + 10, y_offset, true);
		y_offset += 150;

		draw(receiveDesc, box_x + 20, y_offset);
		y_offset += receiveDesc.getHeight() + 10;

		drawResourceBoxes(box_x + 10, y_offset, false);
		y_offset += 200;

		y_offset = box_y + HEIGHT - tradeConfirmButton.getImage().getHeight() - 20;
		draw(tradeConfirmButton.getImage(), (box_x + WIDTH - tradeConfirmButton.getImage().getWidth() - 20)
				- counterProposeButton.getImage().getWidth() - 10 - orText.getWidth() - 10, y_offset);
		draw(orText, box_x + WIDTH - counterProposeButton.getImage().getWidth() - 20 - orText.getWidth() - 10,
				y_offset + counterProposeButton.getImage().getHeight() / 2 - orText.getHeight() / 2);
		draw(counterProposeButton.getImage(), box_x + WIDTH - counterProposeButton.getImage().getWidth() - 20, y_offset);
		
	}

	private void drawResourceBoxes(int x, int y, boolean isOffer) {
		int spacing = (WIDTH - 40) / ResourceType.values().length;
		if (rAmtBox == null || rAmtBoxBorder == null) {
			rAmtBox = new MinuetoRectangle(spacing - 30, 100, MinuetoColor.WHITE, true);
			rAmtBoxBorder = new MinuetoRectangle(spacing - 30, 100, border_color, false);
		}

		for (int i = 0; i < ResourceType.values().length; i++) {
			ResourceType rType = ResourceType.values()[i];
			String rname = rType.toString().toLowerCase();
			rname = rname.substring(0, 1).toUpperCase() + rname.substring(1);

			MinuetoText rnameImage = new MinuetoText(rname, description_font_bold, MinuetoColor.BLACK);

			int amt = isOffer ? give.get(rType) : get.get(rType);
			MinuetoText amtTextImage = new MinuetoText(amt + "", title_font, MinuetoColor.BLACK);

			int y_offset = y;

			int amt_box_x = (x + i * spacing) + (spacing / 2 - rAmtBox.getWidth() / 2);
			draw(rAmtBox, amt_box_x, y_offset);
			draw(amtTextImage, amt_box_x + (rAmtBox.getWidth() / 2) - (amtTextImage.getWidth() / 2),
					y_offset + (rAmtBox.getHeight() / 2) - (amtTextImage.getHeight() / 2));
			draw(rAmtBoxBorder, amt_box_x, y_offset);
			drawPlusMinusButton(true, rType, amt_box_x, y_offset, rAmtBox.getWidth(), rAmtBox.getHeight(), isOffer);
			drawPlusMinusButton(false, rType, amt_box_x, y_offset, rAmtBox.getWidth(), rAmtBox.getHeight(), isOffer);
			y_offset += rAmtBox.getHeight() + 10;

			draw(rnameImage, (x + i * spacing) + (spacing / 2 - rnameImage.getWidth() / 2), y_offset);
		}

	}

	private void drawPlusMinusButton(boolean isPlus, ResourceType rType, int x, int y, int boxWidth, int boxHeight,
			boolean isOffer) {

		HashMap<ResourceType, MinuetoImage> list = isPlus ? plusButtons_offer : minusButtons_offer;
		if (!isOffer)
			list = isPlus ? plusButtons_price : minusButtons_price;

		MinuetoImage image = list.get(rType);

		if (image == null) {
			ImageFileManager ifm = ClientModel.instance.getImageFileManager();
			image = ifm.load("images/plusminus.png");
			image = isPlus ? image.rotate(0) : image.rotate(-Math.PI);
			list.put(rType, image);
		}

		int btn_x = x + (boxWidth / 2 - image.getWidth() / 2);
		int btn_y = isPlus ? y + 10 : y + boxHeight - image.getHeight() - 10;

		registerClickable(image, new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				HashMap<ResourceType, Integer> map = isOffer ? give : get;
				if (isPlus) {
					if (!(isOffer && map.get(rType) >= gsm.getResources().get(rType)))
						map.put(rType, map.get(rType) + 1);
				} else {
					if (map.get(rType) > 0)
						map.put(rType, map.get(rType) - 1);
				}
			}
		});

		draw(image, btn_x, btn_y);
	}

	private ClickListener getCounterProposeListener() {
		return new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				System.out.println("Counter Propose!");
				//here do command
				gsm.setShowTradeMenu(false);
			}
		};
	}

	private ClickListener getTradeConfirmListener() {
		return new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				System.out.println("Confirm Trade");
				//confirm trade here
				gsm.setShowTradeMenu(false);
			}
		};
	}

	private HashMap<ResourceType, Integer> resetResourceMap() {
		HashMap<ResourceType, Integer> ret = new HashMap<>();
		for (ResourceType rType : ResourceType.values()) {
			ret.put(rType, 0);
		}
		return ret;
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
