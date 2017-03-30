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
import catan.settlers.client.model.NetworkManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.network.server.commands.game.SevenDiscardCommand;
import catan.settlers.server.model.Player.ResourceType;

public class SevenDiscardMenuLayer extends ImageLayer {

	private static final HashMap<ResourceType, MinuetoImage> plusButtons = new HashMap<>();
	private static final HashMap<ResourceType, MinuetoImage> minusButtons = new HashMap<>();

	private static final int WIDTH = 1000, HEIGHT = 625;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont description_font = new MinuetoFont("arial", 17, false, false);
	private static final MinuetoFont description_font_bold = new MinuetoFont("arial", 17, true, false);
	private static final MinuetoColor confirm_btn_color = new MinuetoColor(55, 200, 113);

	private int box_x;
	private int box_y;

	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private MinuetoText description;
	private MinuetoText message;
	private MinuetoRectangle rAmtBox;
	private MinuetoRectangle rAmtBoxBorder;
	private Button confirmButton;

	private HashMap<ResourceType, Integer> resources;
	private int nbResToSelect;
	private boolean clear;

	public SevenDiscardMenuLayer() {
		super();

		String title = "A seven was rolled and you have too many resources";
		String description = "You need to discard half of your resources:";

		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = (ClientWindow.WINDOW_HEIGHT + 100) / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);
		this.title = new MinuetoText(title, title_font, MinuetoColor.BLACK);
		this.description = new MinuetoText(description, description_font, MinuetoColor.BLACK);
		this.confirmButton = new Button(this, "Discard resources", confirm_btn_color, getConfirmListener());

		this.resources = resetResourceMap();
		this.nbResToSelect = updateNbResToSelect();
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doShowSevenDiscardMenu()) {
			if (clear) {
				resources = resetResourceMap();
				ClientWindow.getInstance().getGameWindow().clearLayerClickables(this);
				clear();
				clear = false;
			}
			return;
		} else {
			clear = true;
			nbResToSelect = updateNbResToSelect();
		}
		
		draw(background, box_x, box_y);
		draw(border, box_x, box_y);
		overrideClickables();

		int y_offset = box_y + 20;

		draw(title, box_x + (WIDTH / 2 - title.getWidth() / 2), y_offset);
		y_offset += title.getHeight() + 10;

		draw(description, box_x + (WIDTH / 2 - description.getWidth() / 2), y_offset);
		y_offset += description.getHeight() + 10;

		if (!gsm.getSevenDiscardMenuMsg().isEmpty()) {
			message = new MinuetoText(gsm.getSevenDiscardMenuMsg(), description_font_bold, MinuetoColor.RED);
			draw(message, box_x + (WIDTH / 2 - message.getWidth() / 2), y_offset);
		}

		y_offset += 140;

		drawResourceBoxes(box_x + 10, y_offset);
		y_offset += 200;

		y_offset = box_y + HEIGHT - 100;
		int nbSelectedRes = nbOfSelectedResources();
		MinuetoText selectedResourcesMsg = new MinuetoText(nbSelectedRes + "/" + nbResToSelect + " resources selected",
				description_font, MinuetoColor.BLACK);
		draw(selectedResourcesMsg, box_x + WIDTH - selectedResourcesMsg.getWidth() - 20, y_offset);
		y_offset += selectedResourcesMsg.getHeight() + 10;

		draw(confirmButton.getImage(), box_x + WIDTH - confirmButton.getImage().getWidth() - 20, y_offset);
	}

	private void drawResourceBoxes(int x, int y) {
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

			int amt = resources.get(rType);
			MinuetoText amtTextImage = new MinuetoText(amt + "", title_font, MinuetoColor.BLACK);

			int y_offset = y;

			int amt_box_x = (x + i * spacing) + (spacing / 2 - rAmtBox.getWidth() / 2);
			draw(rAmtBox, amt_box_x, y_offset);
			draw(amtTextImage, amt_box_x + (rAmtBox.getWidth() / 2) - (amtTextImage.getWidth() / 2),
					y_offset + (rAmtBox.getHeight() / 2) - (amtTextImage.getHeight() / 2));
			draw(rAmtBoxBorder, amt_box_x, y_offset);
			drawPlusMinusButton(true, rType, amt_box_x, y_offset, rAmtBox.getWidth(), rAmtBox.getHeight());
			drawPlusMinusButton(false, rType, amt_box_x, y_offset, rAmtBox.getWidth(), rAmtBox.getHeight());
			y_offset += rAmtBox.getHeight() + 10;

			draw(rnameImage, (x + i * spacing) + (spacing / 2 - rnameImage.getWidth() / 2), y_offset);
		}

	}

	private void drawPlusMinusButton(boolean isPlus, ResourceType rType, int x, int y, int boxWidth, int boxHeight) {

		HashMap<ResourceType, MinuetoImage> list = isPlus ? plusButtons : minusButtons;

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
				if (isPlus) {
					if (resources.get(rType) < gsm.getResources().get(rType))
						resources.put(rType, resources.get(rType) + 1);
				} else {
					if (resources.get(rType) > 0)
						resources.put(rType, resources.get(rType) - 1);
				}
			}
		});

		draw(image, btn_x, btn_y);
	}

	private HashMap<ResourceType, Integer> resetResourceMap() {
		HashMap<ResourceType, Integer> ret = new HashMap<>();
		for (ResourceType rType : ResourceType.values()) {
			ret.put(rType, 0);
		}
		return ret;
	}

	private ClickListener getConfirmListener() {
		return new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();

				int nbSelectedResources = nbOfSelectedResources();
				if (nbSelectedResources != nbResToSelect) {
					gsm.setSevenDiscardMenuMsg("You need to select exactly " + nbResToSelect + " resources.");
				} else {
					NetworkManager nm = ClientModel.instance.getNetworkManager();
					nm.sendCommand(new SevenDiscardCommand(resources));
					gsm.setSevenDiscardMenuMsg("Command sent!");
				}
			}
		};
	}

	private int nbOfSelectedResources() {
		int count = 0;
		for (ResourceType rType : ResourceType.values()) {
			count += resources.get(rType);
		}
		return count;
	}

	private int updateNbResToSelect() {
		GameStateManager gsm = ClientModel.instance.getGameStateManager();
		int count = 0;
		for (ResourceType rtype : ResourceType.values()) {
			count += gsm.getResources().get(rtype);
		}
		return (int) Math.floor(count / 2);
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
