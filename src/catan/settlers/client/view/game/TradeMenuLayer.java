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
import catan.settlers.network.server.commands.game.MaritimeTradeCommand;
import catan.settlers.network.server.commands.game.PlayerTradeRequestCommand;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.units.Port.PortKind;

public class TradeMenuLayer extends ImageLayer {

	private static final HashMap<ResourceType, MinuetoImage> plusButtons_offer = new HashMap<>();
	private static final HashMap<ResourceType, MinuetoImage> minusButtons_offer = new HashMap<>();
	private static final HashMap<ResourceType, MinuetoImage> plusButtons_price = new HashMap<>();
	private static final HashMap<ResourceType, MinuetoImage> minusButtons_price = new HashMap<>();

	private static final int WIDTH = 1000, HEIGHT = 635;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	private static final MinuetoColor bank_confirm_btn_color = new MinuetoColor(55, 200, 113);
	private static final MinuetoColor player_confirm_btn_color = new MinuetoColor(255, 153, 85);
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont description_font = new MinuetoFont("arial", 17, false, false);
	private static final MinuetoFont description_font_bold = new MinuetoFont("arial", 17, true, false);

	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private MinuetoText description;
	private MinuetoText giveDesc;
	private MinuetoText receiveDesc;
	private Button bankConfirmButton;
	private MinuetoRectangle rAmtBox;
	private MinuetoRectangle rAmtBoxBorder;
	private Button playerConfirmButton;
	private MinuetoText orText;
	private String greetingMessage;

	private boolean clear;
	private int box_x, box_y;
	private HashMap<ResourceType, Integer> give, get;

	public TradeMenuLayer() {
		super();

		this.greetingMessage = "Welcome to the trade menu!";
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		gsm.setTradeMenuMessage(greetingMessage);

		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = (ClientWindow.WINDOW_HEIGHT + 100) / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);
		this.title = new MinuetoText("Trade", title_font, MinuetoColor.BLACK);
		this.description = new MinuetoText("Exchange resources with other players or with the bank", description_font,
				MinuetoColor.BLACK);
		this.giveDesc = new MinuetoText("You propose:", description_font_bold, MinuetoColor.BLACK);
		this.receiveDesc = new MinuetoText("In exchange of:", description_font_bold, MinuetoColor.BLACK);
		this.bankConfirmButton = new Button(this, "Trade with bank", bank_confirm_btn_color, getBankConfirmListener());
		this.playerConfirmButton = new Button(this, "Send offer to the other players", player_confirm_btn_color,
				getPlayerConfirmListener());
		this.orText = new MinuetoText("or", description_font, MinuetoColor.BLACK);

		this.give = resetResourceMap();
		this.get = resetResourceMap();
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doShowTradeMenu()) {
			if (clear) {
				give = resetResourceMap();
				get = resetResourceMap();
				ClientWindow.getInstance().getGameWindow().clearLayerClickables(this);
				clear();
				gsm.setTradeMenuMessage(greetingMessage);
				clear = false;
			}
			return;
		} else {
			if (!gsm.getCurrentPlayer().equals(ClientModel.instance.getUsername())) {
				gsm.setShowTradeMenu(false);
				return;
			}

			clear = true;
		}

		draw(background, box_x, box_y);
		draw(border, box_x, box_y);
		overrideClickables();

		int y_offset = box_y + 20;

		draw(title, box_x + (WIDTH / 2 - title.getWidth() / 2), y_offset);
		y_offset += title.getHeight() + 10;

		draw(description, box_x + (WIDTH / 2 - description.getWidth() / 2), y_offset);
		y_offset += description.getHeight() + 20;

		MinuetoText message = new MinuetoText(gsm.getTradeMenuMsg(), description_font_bold, MinuetoColor.BLACK);
		draw(message, box_x + (WIDTH / 2 - message.getWidth() / 2), y_offset);
		y_offset += message.getHeight() + 20;

		y_offset += drawPortButtons(box_x + 20, y_offset);

		draw(giveDesc, box_x + 20, y_offset);
		y_offset += giveDesc.getHeight() + 10;

		drawResourceBoxes(box_x + 10, y_offset, true);
		y_offset += 150;

		draw(receiveDesc, box_x + 20, y_offset);
		y_offset += receiveDesc.getHeight() + 10;

		drawResourceBoxes(box_x + 10, y_offset, false);
		y_offset += 200;

		y_offset = box_y + HEIGHT - playerConfirmButton.getImage().getHeight() - 20;
		draw(playerConfirmButton.getImage(), (box_x + WIDTH - playerConfirmButton.getImage().getWidth() - 20)
				- bankConfirmButton.getImage().getWidth() - 10 - orText.getWidth() - 10, y_offset);
		draw(orText, box_x + WIDTH - bankConfirmButton.getImage().getWidth() - 20 - orText.getWidth() - 10,
				y_offset + bankConfirmButton.getImage().getHeight() / 2 - orText.getHeight() / 2);
		draw(bankConfirmButton.getImage(), box_x + WIDTH - bankConfirmButton.getImage().getWidth() - 20, y_offset);
	}

	private int drawPortButtons(int x, int y) {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		boolean buttonAdded = false;

		int y_offset = y;
		int x_offset = x;

		for (PortKind pkind : PortKind.values()) {
			boolean ownsPortKind = gsm.getOwnedPorts().get(pkind);
			if (ownsPortKind) {
				buttonAdded = true;
				String portName = "";
				switch (pkind) {
				case ALLPORT:
					portName = "General port (3:1)";
					break;
				case BRICKPORT:
					portName = "Brick port (2:1)";
					break;
				case LUMBERPORT:
					portName = "Lumber port (2:1)";
					break;
				case OREPORT:
					portName = "Ore port (2:1)";
					break;
				case WOOLPORT:
					portName = "Wool port (2:1)";
					break;
				case GRAINPORT:
					portName = "Wheat port (2:1)";
					break;
				}

				Button button = new Button(this, portName, new MinuetoColor(255, 238, 170), new ClickListener() {

					@Override
					public void onClick() {
						for (ResourceType rtype : ResourceType.values()) {
							give.put(rtype, 0);
							get.put(rtype, 0);
						}

						switch (pkind) {
						case ALLPORT:
							gsm.setTradeMenuMessage(
									"General port. Select 3 resources of any kind in your offer, and 1 resource that you want to receive in exchange.");
							break;
						case LUMBERPORT:
							give.put(ResourceType.LUMBER, 2);
							gsm.setTradeMenuMessage("Lumber port. Select the resource that you want to get.");
							break;
						case BRICKPORT:
							give.put(ResourceType.BRICK, 2);
							gsm.setTradeMenuMessage("Brick port. Select the resource that you want to get.");
							break;
						case OREPORT:
							give.put(ResourceType.ORE, 2);
							gsm.setTradeMenuMessage("Ore port. Select the resource that you want to get.");
							break;
						case GRAINPORT:
							give.put(ResourceType.GRAIN, 2);
							gsm.setTradeMenuMessage("Grain port. Select the resource that you want to get.");
							break;
						case WOOLPORT:
							give.put(ResourceType.WOOL, 2);
							gsm.setTradeMenuMessage("Wool port. Select the resource that you want to get.");
							break;
						default:
							break;
						}
					}
				});

				draw(button.getImage(), x_offset, y_offset);

				x_offset += button.getImage().getWidth() + 15;

				if (x_offset > box_x + WIDTH) {
					x_offset = x;
					y_offset += button.getImage().getHeight() + 15;
				}
			}
		}
		return buttonAdded ? y - y_offset + 60 : 0;
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

	private ClickListener getBankConfirmListener() {
		return new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				System.out.println("Trade with bank!");
				ClientModel.instance.getNetworkManager().sendCommand(new MaritimeTradeCommand(give, get));
				gsm.setShowTradeMenu(false);
			}
		};
	}

	private ClickListener getPlayerConfirmListener() {
		return new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				System.out.println("Trade with players!");
				ClientModel.instance.getNetworkManager().sendCommand(new PlayerTradeRequestCommand(give, get));
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
