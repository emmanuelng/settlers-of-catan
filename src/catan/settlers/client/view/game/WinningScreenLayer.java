package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.client.view.setup.MainMenu;
import catan.settlers.network.server.commands.game.ExitGameCommand;

public class WinningScreenLayer extends ImageLayer {

	private static final int WIDTH = 1000, HEIGHT = 695;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont description_font = new MinuetoFont("arial", 17, false, false);
	private static final MinuetoFont description_font_bold = new MinuetoFont("arial", 17, true, false);
	private static final MinuetoFont btn_font = new MinuetoFont("arial", 16, true, false);
	private static final MinuetoColor btn_color = new MinuetoColor(55, 200, 113);
	
	
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private MinuetoText description;
	private MinuetoText exitButtonText;
	private MinuetoRectangle exitButtonBg;
	private MinuetoRectangle exitButtonShadow;
	private int box_x, box_y;
	
	public WinningScreenLayer(){
		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = (ClientWindow.WINDOW_HEIGHT + 100) / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);
		
		this.exitButtonText = new MinuetoText("Exit", btn_font, btn_color.darken(0.3));
		this.exitButtonBg = new MinuetoRectangle(100, exitButtonText.getHeight() + 30, btn_color, true);
		this.exitButtonShadow = new MinuetoRectangle(100, exitButtonText.getHeight() + 30, btn_color.darken(0.3),
				true);
	}
	
	@Override
	public void compose(GameStateManager gsm) {
		if(!gsm.getWinningScreen()){
			return;
		}
		
		int x_offset = box_x + WIDTH /2 - 30;
		int y_offset = box_y + 20;
		
		draw(background, box_x, box_y);
		draw(border, box_x, box_y);
		overrideClickables();

		if(gsm.getWinner() != null){
			if(gsm.getWinner().equals(gsm.getCurrentPlayer())){
				this.title = new MinuetoText("CONGRATULATIONS", title_font, MinuetoColor.BLACK);
				this.description = new MinuetoText("You are the winner", description_font, MinuetoColor.BLACK);
				draw(title,box_x + (WIDTH / 2 - title.getWidth() / 2), y_offset);
				y_offset += title.getHeight() + 10;
				

				draw(description, box_x + (WIDTH / 2 - description.getWidth() / 2), y_offset);
				y_offset += description.getHeight() + 20;
			}else{
				this.title = new MinuetoText("GAME OVER", title_font, MinuetoColor.BLACK);
				this.description = new MinuetoText(gsm.getWinner() + " is the winner", description_font, MinuetoColor.BLACK);
				draw(title,box_x + (WIDTH / 2 - title.getWidth() / 2), y_offset);
				y_offset += title.getHeight() + 10;
				

				draw(description, box_x + (WIDTH / 2 - description.getWidth() / 2), y_offset);
				y_offset += description.getHeight() + 20;
			}
		}
		
		draw(exitButtonShadow, x_offset + 3, y_offset + 3);
		draw(exitButtonBg, x_offset, y_offset);
		draw(exitButtonText, x_offset + exitButtonBg.getWidth() / 2 - exitButtonText.getWidth() / 2,
				y_offset + exitButtonBg.getHeight() / 2 );
		y_offset += exitButtonBg.getHeight() + 15;
		
		registerClickable(exitButtonBg, new ClickListener() {
			@Override
			public void onClick() {
				NetworkManager nm = ClientModel.instance.getNetworkManager();
				nm.sendCommand(new ExitGameCommand());
				ClientWindow.getInstance().getSetupWindow().setScreen(new MainMenu());
				ClientWindow.getInstance().switchToSetup();
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
