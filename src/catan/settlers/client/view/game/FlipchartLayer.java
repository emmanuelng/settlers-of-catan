package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.server.model.units.Knight;

public class FlipchartLayer extends ImageLayer {

	private static enum Field {
		TRADE, POLITICS, SCIENCE
	};

	private static final int WIDTH = 950, HEIGHT = 500;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	private static final MinuetoFont title_font = new MinuetoFont("arial", 18, true, false);
	private static final MinuetoFont field_title_font = new MinuetoFont("arial", 25, true, false);
	private static final MinuetoFont field_description_font = new MinuetoFont("arial", 20, false, false);
	private static final MinuetoFont description_font = new MinuetoFont("arial", 17, false, false);
	private static final MinuetoFont description_font_bold = new MinuetoFont("arial", 17, true, false);

	private Field currentField;

	private int box_x, box_y;
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private int leftBarWidth;
	private MinuetoRectangle leftBarBg;
	private MinuetoRectangle tradeButtonBg;
	private MinuetoText tradeText;
	private MinuetoImage politicsButtonBg;
	private MinuetoText politicsText;
	private MinuetoImage scienceButtonBg;
	private MinuetoImage scienceText;
	private Button levelUpBtn;

	public FlipchartLayer() {
		
		this.currentField = Field.TRADE;
		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = ClientWindow.WINDOW_HEIGHT / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);

		this.title = new MinuetoText("CITY IMPROVEMENTS", title_font, new MinuetoColor(244, 227, 215));
		this.leftBarWidth = title.getWidth() + 40;
		this.leftBarBg = new MinuetoRectangle(leftBarWidth, HEIGHT, new MinuetoColor(222, 170, 135), true);

		this.tradeButtonBg = new MinuetoRectangle(leftBarWidth - 10, 35, new MinuetoColor(255, 221, 85), true);
		this.tradeText = new MinuetoText("Trade", description_font_bold, new MinuetoColor(170, 136, 0));

		this.politicsButtonBg = new MinuetoRectangle(leftBarWidth - 10, 35, new MinuetoColor(135, 205, 222), true);
		this.politicsText = new MinuetoText("Politics", description_font_bold, new MinuetoColor(44, 137, 160));

		this.scienceButtonBg = new MinuetoRectangle(leftBarWidth - 10, 35, new MinuetoColor(55, 200, 113), true);
		this.scienceText = new MinuetoText("Science", description_font_bold, new MinuetoColor(33, 120, 68));

		this.levelUpBtn = new Button(this, "Go to next improvement level", new MinuetoColor(55, 200, 113),
				new ClickListener() {

					@Override
					public void onClick() {
						System.out.println("Level up!");
					}
				});
	}

	@Override
	public void compose(GameStateManager gsm) {
		draw(background, box_x, box_y);
		draw(leftBarBg, box_x, box_y);

		drawLeftBar();
		drawRightPart(gsm);

		draw(border, box_x, box_y);

	}

	private void drawLeftBar() {
		int y_offset = box_y + 20;

		draw(title, box_x + leftBarWidth / 2 - title.getWidth() / 2, y_offset);
		y_offset += title.getHeight() + 30;

		draw(tradeButtonBg, box_x + 5, y_offset);
		draw(tradeText, box_x + 5 + tradeButtonBg.getWidth() / 2 - tradeText.getWidth() / 2,
				y_offset + tradeButtonBg.getHeight() / 2 - tradeText.getHeight() / 2);
		y_offset += tradeButtonBg.getHeight() + 10;

		draw(politicsButtonBg, box_x + 5, y_offset);
		draw(politicsText, box_x + 5 + politicsButtonBg.getWidth() / 2 - politicsText.getWidth() / 2,
				y_offset + politicsButtonBg.getHeight() / 2 - politicsText.getHeight() / 2);
		y_offset += politicsButtonBg.getHeight() + 10;

		draw(scienceButtonBg, box_x + 5, y_offset);
		draw(scienceText, box_x + 5 + scienceButtonBg.getWidth() / 2 - scienceText.getWidth() / 2,
				y_offset + scienceButtonBg.getHeight() / 2 - scienceText.getHeight() / 2);

		registerLeftBarClickables();
	}

	private void registerLeftBarClickables() {

		// Trade button
		registerClickable(tradeButtonBg, new ClickListener() {
			@Override
			public void onClick() {
				currentField = Field.TRADE;
			}
		});

		// Politics button
		registerClickable(politicsButtonBg, new ClickListener() {
			@Override
			public void onClick() {
				currentField = Field.POLITICS;
			}
		});

		// Science button
		registerClickable(scienceButtonBg, new ClickListener() {
			@Override
			public void onClick() {
				currentField = Field.SCIENCE;
			}
		});
	}

	private void drawRightPart(GameStateManager gsm) {
		int x_offset = box_x + leftBarWidth + 15;
		int y_offset = box_y + 20;

		MinuetoText fieldTitle = new MinuetoText(currentField.toString(), field_title_font, MinuetoColor.BLACK);
		draw(fieldTitle, x_offset, y_offset);
		y_offset += fieldTitle.getHeight() + 10;

		int level = 0;
		switch (currentField) {
		case TRADE:
			level = gsm.getTradeImprovementLevel();
			break;
		case POLITICS:
			level = gsm.getPoliticsImprovementLevel();
			break;
		case SCIENCE:
			level = gsm.getScienceImprovementLevel();
			break;
		}

		MinuetoText fieldLevel = new MinuetoText("Level " + level, field_description_font, MinuetoColor.BLACK);
		draw(fieldLevel, x_offset, y_offset);
		y_offset += fieldLevel.getHeight() + 30;

		MinuetoText buildingText = new MinuetoText("BUILDINGS", title_font, MinuetoColor.BLACK);
		draw(buildingText, x_offset, y_offset);
		y_offset += buildingText.getHeight() + 15;

		HashMap<String, String> buildings = getBuildingDescriptions(gsm);

		MinuetoImage levelUp = levelUpBtn.getImage();
		draw(levelUp, box_x + WIDTH - levelUp.getWidth() - 10, box_y + HEIGHT - levelUp.getHeight() - 10);
	}

	private HashMap<String, String> getBuildingDescriptions(GameStateManager gsm) {
		HashMap<String, String> ret = new HashMap<>();

		switch (currentField) {
		case TRADE:
			ret = getTradeBuildings(gsm);
			break;
		case POLITICS:
			ret = getPoliticsBuildings(gsm);
			break;
		case SCIENCE:
			ret = getScienceBuildings(gsm);
			break;
		}

		return ret;
	}

	private HashMap<String, String> getTradeBuildings(GameStateManager gsm) {
		HashMap<String, String> ret = new HashMap<>();
		switch (gsm.getTradeImprovementLevel()) {
		case 0:
			ret.put("Road", "1 brick and 1 lumber");
			ret.put("Ship", "1 lumber and 1 wool");
			ret.put("Settlement", "1 brick, 1 lumber, 1 wool and 1 grain");
			break;

		default:
			break;
		}
		return ret;
	}

	private HashMap<String, String> getPoliticsBuildings(GameStateManager gsm) {
		HashMap<String, String> ret = new HashMap<>();
		switch (gsm.getTradeImprovementLevel()) {
		case 0:
			ret.put("City", "2 grain and 3 ore");
			ret.put("City wall", "2 bricks");
			break;

		default:
			break;
		}
		return ret;
	}

	private HashMap<String, String> getScienceBuildings(GameStateManager gsm) {
		HashMap<String, String> ret = new HashMap<>();
		switch (gsm.getTradeImprovementLevel()) {
		case 0:
			ret.put("Knight", "1 wool and 1 ore");
			ret.put("Knight promotion", "1 wool and 1 ore");
			ret.put("Knight activation", "1 grain");
			break;

		default:
			break;
		}
		return ret;
	}

}
