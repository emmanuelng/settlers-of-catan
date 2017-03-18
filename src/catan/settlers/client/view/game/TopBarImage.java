package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoCircle;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.ImageFileManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.server.model.Player.ResourceType;

public class TopBarImage extends MinuetoImage {

	private HashMap<ResourceType, Integer> resources;

	public TopBarImage() {
		super(ClientWindow.WINDOW_WIDTH, ClientWindow.WINDOW_HEIGHT);

		this.resources = ClientModel.instance.getGameStateManager().getResources();

		if (resources == null) {
			this.resources = new HashMap<>();
			for (ResourceType r : ResourceType.values()) {
				this.resources.put(r, 0);
			}
		}

		compose();
	}

	private void compose() {
		draw(new MinuetoRectangle(ClientWindow.WINDOW_WIDTH, 105, new MinuetoColor(236, 236, 236), true), 0, 0);
		draw(new MinuetoRectangle(ClientWindow.WINDOW_WIDTH, 100, new MinuetoColor(249, 249, 249), true), 0, 0);

		drawResource(ResourceType.GRAIN, 100, 25);
		drawResource(ResourceType.LUMBER, 250, 25);
		drawResource(ResourceType.ORE, 400, 25);
		drawResource(ResourceType.WOOL, 550, 25);
		drawResource(ResourceType.BRICK, 700, 25);
		drawResource(ResourceType.CLOTH, 100, 65);
		drawResource(ResourceType.PAPER, 250, 65);
		drawResource(ResourceType.COIN, 400, 65);

		drawDice(900, 25);
		drawTrade(1000, 25);
		drawEndTurn(1100, 25);
		drawVP(1200, 0);
	}

	private void drawResource(ResourceType r, int x, int y) {
		MinuetoColor color;
		MinuetoFont resourceFont = new MinuetoFont("arial", 17, true, false);
		MinuetoFont amountFont = new MinuetoFont("arial", 17, false, false);

		switch (r) {
		case GRAIN:
			color = new MinuetoColor(198, 233, 175);
			break;
		case LUMBER:
			color = new MinuetoColor(222, 170, 135);
			break;
		case ORE:
			color = new MinuetoColor(200, 190, 183);
			break;
		case WOOL:
			color = new MinuetoColor(255, 246, 213);
			break;
		case BRICK:
			color = new MinuetoColor(255, 153, 85);
			break;
		case CLOTH:
			color = new MinuetoColor(255, 230, 213);
			break;
		case PAPER:
			color = new MinuetoColor(246, 255, 213);
			break;
		default:
			color = new MinuetoColor(198, 233, 175);
			break;
		}

		MinuetoCircle circleImage = new MinuetoCircle(10, color, true);
		MinuetoText resourceNameImage = new MinuetoText("" + r, resourceFont, MinuetoColor.BLACK, true);

		MinuetoText typeAmountImage = new MinuetoText("" + resources.get(r), amountFont, MinuetoColor.BLACK, true);
		if (resources.get(r) == null) {
			typeAmountImage = new MinuetoText("0", amountFont, MinuetoColor.BLACK, true);
		}

		draw(circleImage, x, y);
		draw(resourceNameImage, x + circleImage.getWidth() + 10, y);
		draw(typeAmountImage, x + circleImage.getWidth() + resourceNameImage.getWidth() + 15, y);
	}

	private void drawDice(int x, int y) {
		DiceImage diceImage = new DiceImage(x, y);
		this.draw(diceImage, x, y);
		ClientWindow.getInstance().getGameWindow().getMouseHandler().register(diceImage);
	}

	private void drawTrade(int x, int y) {
		TradeImage tradeImage = new TradeImage(x, y);
		this.draw(tradeImage, x, y);
		ClientWindow.getInstance().getGameWindow().getMouseHandler().register(tradeImage);
	}

	private void drawEndTurn(int x, int y) {
		EndTurnImage endTurnImage = new EndTurnImage(x, y);
		this.draw(endTurnImage, x, y);
		ClientWindow.getInstance().getGameWindow().getMouseHandler().register(endTurnImage);
	}

	private void drawVP(int x, int y) {
		ImageFileManager ifm = ClientModel.instance.getImageFileManager();
		draw(ifm.load("images/vp-new.png"), x, y);
		draw(new MinuetoText("0", new MinuetoFont("arial", 25, true, false), new MinuetoColor(200, 171, 55), true),
				x + 75, y + 35);
	}
}
