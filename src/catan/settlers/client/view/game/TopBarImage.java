package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoCircle;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoText;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.server.model.Player.ResourceType;

public class TopBarImage extends MinuetoImage {

	private HashMap<ResourceType, Integer> resources;

	public TopBarImage(HashMap<ResourceType, Integer> resources) {
		super(ClientWindow.getInstance().getGameWindow().getWidth(), 100);

		this.resources = resources;
		compose(0, 0);
	}

	private void compose(int x, int y) {
		clear(new MinuetoColor(249, 249, 249));

		drawResource(ResourceType.GRAIN, x + 100, 25);
		drawResource(ResourceType.LUMBER, x + 250, 25);
		drawResource(ResourceType.ORE, x + 400, 25);
		drawResource(ResourceType.WOOL, x + 550, 25);
		drawResource(ResourceType.BRICK, x + 700, 25);
		drawResource(ResourceType.CLOTH, x + 100, 65);
		drawResource(ResourceType.PAPER, x + 250, 65);
		drawResource(ResourceType.COIN, x + 400, 65);

		drawDice(x + 900, 25);
		drawTrade(x + 1000, 25);
		drawEndTurn(x + 1100, 25);
		drawVP(x + 1200, 25);
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
		MinuetoImage vp;
		try {
			vp = new MinuetoImageFile("images/vp.png");
			draw(vp, x, y);
		} catch (MinuetoFileException e) {
			System.out.println("Could not load image file");
			return;
		}
		draw(new MinuetoText("0", new MinuetoFont("arial", 25, false, false), MinuetoColor.BLACK), x + 60, y + 10);
	}
}
