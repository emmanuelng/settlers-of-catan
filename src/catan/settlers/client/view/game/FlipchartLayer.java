package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.network.server.commands.game.IncrementPoliticsCommand;
import catan.settlers.network.server.commands.game.IncrementScienceCommand;
import catan.settlers.network.server.commands.game.IncrementTradeCommand;

public class FlipchartLayer extends ImageLayer {

	public static enum Field {
		TRADE, POLITICS, SCIENCE
	};

	private static final int WIDTH = 950, HEIGHT = 500;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	private static final MinuetoFont title_font = new MinuetoFont("arial", 18, true, false);
	private static final MinuetoFont field_title_font = new MinuetoFont("arial", 25, true, false);
	private static final MinuetoFont field_description_font = new MinuetoFont("arial", 20, false, false);
	private static final MinuetoFont description_font_bold = new MinuetoFont("arial", 17, true, false);

	private Field currentField;

	private int box_x, box_y;
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private int leftBarWidth;
	private int rightPartWidth;
	private MinuetoRectangle leftBarBg;
	private MinuetoRectangle rightPartBg;
	private MinuetoRectangle tradeButtonBg;
	private MinuetoText tradeText;
	private MinuetoImage politicsButtonBg;
	private MinuetoText politicsText;
	private MinuetoImage scienceButtonBg;
	private MinuetoImage scienceText;
	private Button levelUpBtn;
	private boolean clear;
	
	public FlipchartLayer() {

		this.currentField = Field.TRADE;
		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = ClientWindow.WINDOW_HEIGHT / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);

		this.title = new MinuetoText("CITY IMPROVEMENTS", title_font, new MinuetoColor(244, 227, 215));
		this.leftBarWidth = title.getWidth() + 40;
		this.leftBarBg = new MinuetoRectangle(leftBarWidth, HEIGHT, new MinuetoColor(222, 170, 135), true);

		this.rightPartWidth = WIDTH - leftBarWidth;
		this.rightPartBg = new MinuetoRectangle(rightPartWidth, HEIGHT, bg_color , true);
		
		this.tradeButtonBg = new MinuetoRectangle(leftBarWidth - 10, 35, new MinuetoColor(255, 221, 85), true);
		this.tradeText = new MinuetoText("Trade", description_font_bold, new MinuetoColor(170, 136, 0));

		this.politicsButtonBg = new MinuetoRectangle(leftBarWidth - 10, 35, new MinuetoColor(135, 205, 222), true);
		this.politicsText = new MinuetoText("Politics", description_font_bold, new MinuetoColor(44, 137, 160));

		this.scienceButtonBg = new MinuetoRectangle(leftBarWidth - 10, 35, new MinuetoColor(55, 200, 113), true);
		this.scienceText = new MinuetoText("Science", description_font_bold, new MinuetoColor(33, 120, 68));
		
		this.levelUpBtn = new Button(this, "Level Up", new MinuetoColor(55, 200, 113),getPlayerConfirmListener());
				
	}

	
	private ClickListener getPlayerConfirmListener() {
		// TODO Auto-generated method stub
		return new ClickListener() {

			@Override
			public void onClick() {
				NetworkManager nm = ClientModel.instance.getNetworkManager();
				switch(currentField){
				case TRADE:
					
					nm.sendCommand(new IncrementTradeCommand());
					break;
				case POLITICS:
					
					nm.sendCommand(new IncrementPoliticsCommand());
					break;
				case SCIENCE:
					
					nm.sendCommand(new IncrementScienceCommand());
					break;
				}
			}
		};
	}


	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.getDoShowFlipchartLayer()) {
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
		draw(leftBarBg, box_x, box_y);
		draw(rightPartBg, box_x+leftBarBg.getWidth(),box_y);
		draw(border, box_x, box_y);
		overrideClickables();
		drawLeftBar();
		drawRightPart(gsm);
		
		
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
		String building = "";
		String buildingDescription = "";
		switch (currentField) {
		case TRADE:
			level = gsm.getTradeImprovementLevel();
			building = "Trading House";
			buildingDescription = "You may now trade 2 commodity for any resource";
			break;
		case POLITICS:
			level = gsm.getPoliticsImprovementLevel();
			building = "Barracks";
			buildingDescription = "You may now hire mighty knights";
			break;
		case SCIENCE:
			level = gsm.getScienceImprovementLevel();
			building = "Aqueduct";
			buildingDescription = "You may now choose to take a resource of your choice when a dice is rolled.";
			break;
		}


		MinuetoText fieldLevel = new MinuetoText("Level " + level, field_description_font, MinuetoColor.BLACK);
		draw(fieldLevel, x_offset, y_offset);
		y_offset += fieldLevel.getHeight() + 30;

		if(level>=3){
			MinuetoText buildingText = new MinuetoText(building, title_font, MinuetoColor.BLACK);
			draw(buildingText, x_offset, y_offset);
			y_offset += buildingText.getHeight() + 15;
			
			MinuetoText buildingDes = new MinuetoText(buildingDescription, field_description_font, MinuetoColor.BLACK);
			draw(buildingDes, x_offset, y_offset);
			y_offset += buildingDes.getHeight() + 15;
		}
		if(gsm.getCurrentPlayer() == gsm.getScienceMetOwner()){
			MinuetoText metropolisText = new MinuetoText("Metropolis of Science", title_font, MinuetoColor.BLACK);
			draw(metropolisText, x_offset, y_offset);
			y_offset += metropolisText.getHeight() + 15;
		}
		if(gsm.getCurrentPlayer() == gsm.getTradeMetOwner()){
			MinuetoText metropolisText = new MinuetoText("Metropolis of Trade", title_font, MinuetoColor.BLACK);
			draw(metropolisText, x_offset, y_offset);
			y_offset += metropolisText.getHeight() + 15;
		}
		if(gsm.getCurrentPlayer() == gsm.getPolMetOwner()){
			MinuetoText metropolisText = new MinuetoText("Metropolis of Politics", title_font, MinuetoColor.BLACK);
			draw(metropolisText, x_offset, y_offset);
			y_offset += metropolisText.getHeight() + 15;
		}

		MinuetoImage levelUp = levelUpBtn.getImage();
		draw(levelUp, box_x + WIDTH - levelUp.getWidth() - 50, box_y + HEIGHT - levelUp.getHeight() - 10);
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
				System.out.println("This is the background");
			}
		});
	}

}
