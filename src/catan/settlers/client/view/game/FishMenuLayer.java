package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.network.server.commands.game.FishActionCommand;
import catan.settlers.server.model.game.handlers.FishHandler.FishAction;

public class FishMenuLayer extends ImageLayer {

	private static final int WIDTH = 500, HEIGHT = 450;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont description_font_bold = new MinuetoFont("arial", 17, true, false);
	private static final MinuetoFont description_font = new MinuetoFont("arial", 16, false, false);
	
	private int box_x, box_y;
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private MinuetoText description;
	private boolean clear;
	private MinuetoRectangle btnShadow;
	private Button removeRobber;
	private Button stealResource;
	private Button drawResource;
	private Button buildRoad;
	private Button devCard;
	
	public FishMenuLayer(){
		super();

		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = (ClientWindow.WINDOW_HEIGHT + 100) / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);
		this.title = new MinuetoText("Trade Fish!", title_font, MinuetoColor.BLACK);
		this.description = new MinuetoText("You have "+ ClientModel.instance.getGameStateManager().getNumFish() + " fish.", description_font, MinuetoColor.BLACK);
	}
	
	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.doShowFishMenu()) {
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
		draw(description,box_x + (WIDTH / 2 - description.getWidth() / 2), y_offset );
		y_offset += 20;
		
		drawRemoveRobber();
		drawStealResource();
		drawResource();
		drawFreeRoad();
		drawDrawDevCard();
		
		
		draw(removeRobber.getImage(), box_x + 100 , y_offset);
		y_offset += removeRobber.getImage().getHeight() + 15;
		draw(stealResource.getImage(), box_x + 100 , y_offset);
		y_offset += stealResource.getImage().getHeight() + 15;
		draw(drawResource.getImage(), box_x + 100 , y_offset);
		y_offset += drawResource.getImage().getHeight() + 15;
		draw(buildRoad.getImage(), box_x + 100 , y_offset);
		y_offset += buildRoad.getImage().getHeight() + 15;
		draw(devCard.getImage(), box_x + 100 , y_offset);
		y_offset += devCard.getImage().getHeight() + 15;
	}
	
	private void drawDrawDevCard() {
		this.devCard = new Button(this, "Draw Dev Card" , MinuetoColor.BLACK, new ClickListener() {

					@Override
					public void onClick() {
						ClientModel.instance.getNetworkManager().sendCommand(new FishActionCommand(FishAction.PROGRESSCARD));
					}
				});
	}

	private void drawFreeRoad() {
		this.buildRoad = new Button(this, "Build Free Road" , MinuetoColor.BLACK, new ClickListener() {

			@Override
			public void onClick() {
				ClientModel.instance.getNetworkManager().sendCommand(new FishActionCommand(FishAction.BUILDROAD));
			}
		}); 
		
	}

	private void drawResource() {
		this.drawResource = new Button(this, "Draw Free Resource" , MinuetoColor.BLACK, new ClickListener() {

			@Override
			public void onClick() {
				ClientModel.instance.getNetworkManager().sendCommand(new FishActionCommand(FishAction.DRAWRESOURCE));
			}
		}); 
		
	}

	private void drawStealResource() {
		this.stealResource = new Button(this, "Steal Resource" , MinuetoColor.BLACK, new ClickListener() {

			@Override
			public void onClick() {
				ClientModel.instance.getNetworkManager().sendCommand(new FishActionCommand(FishAction.STEALRESOURCE));
			}
		}); 
		
	}

	private void drawRemoveRobber() {
		this.removeRobber = new Button(this, "Remove Robber" , MinuetoColor.BLACK, new ClickListener() {

			@Override
			public void onClick() {
				ClientModel.instance.getNetworkManager().sendCommand(new FishActionCommand(FishAction.REMOVEROBBER));
			}
		}); 
		
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
