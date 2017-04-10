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
import catan.settlers.network.server.commands.game.SaveGameCommand;

public class SaveAndExitMenuLayer extends ImageLayer {

	private static final int WIDTH = 500, HEIGHT = 300;
	private static final MinuetoFont btn_font = new MinuetoFont("arial", 16, true, false);
	private static final MinuetoColor btn_color = new MinuetoColor(55, 200, 113);

	private int box_x, box_y;
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText saveButtonText;
	private MinuetoRectangle saveButtonBg;
	private MinuetoRectangle saveButtonShadow;
	private MinuetoText exitButtonText;
	private MinuetoRectangle exitButtonBg;
	private MinuetoRectangle exitButtonShadow;

	public SaveAndExitMenuLayer() {
		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = ClientWindow.WINDOW_HEIGHT / 2 - HEIGHT / 2;

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, new MinuetoColor(249, 249, 249), true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, new MinuetoColor(179, 179, 179), false);

		this.saveButtonText = new MinuetoText("Save game", btn_font, btn_color.darken(0.3));
		this.saveButtonBg = new MinuetoRectangle(WIDTH - 20, saveButtonText.getHeight() + 30, btn_color, true);
		this.saveButtonShadow = new MinuetoRectangle(WIDTH - 20, saveButtonText.getHeight() + 30, btn_color.darken(0.3),
				true);

		this.exitButtonText = new MinuetoText("Exit", btn_font, btn_color.darken(0.3));
		this.exitButtonBg = new MinuetoRectangle(WIDTH - 20, exitButtonText.getHeight() + 30, btn_color, true);
		this.exitButtonShadow = new MinuetoRectangle(WIDTH - 20, exitButtonText.getHeight() + 30, btn_color.darken(0.3),
				true);
	}

	@Override
	public void compose(GameStateManager gsm) {
		draw(background, box_x, box_y);

		int y_offset = box_y + 20, x_offset = box_x + 10;

		draw(saveButtonShadow, x_offset + 3, y_offset + 3);
		draw(saveButtonBg, x_offset, y_offset);
		draw(saveButtonText, x_offset + saveButtonBg.getWidth() / 2 - saveButtonText.getWidth() / 2,
				y_offset + saveButtonBg.getHeight() / 2 - saveButtonText.getHeight() / 2);
		y_offset += saveButtonBg.getHeight() + 15;
		
		draw(exitButtonShadow, x_offset + 3, y_offset + 3);
		draw(exitButtonBg, x_offset, y_offset);
		draw(exitButtonText, x_offset + exitButtonBg.getWidth() / 2 - exitButtonText.getWidth() / 2,
				y_offset + exitButtonBg.getHeight() / 2 - saveButtonText.getHeight() / 2);
		y_offset += exitButtonBg.getHeight() + 15;

		registerClickable(saveButtonBg, new ClickListener() {
			@Override
			public void onClick() {
				NetworkManager nm = ClientModel.instance.getNetworkManager();
				nm.sendCommand(new SaveGameCommand());
			}
		});
		
		registerClickable(exitButtonBg, new ClickListener() {
			@Override
			public void onClick() {
				NetworkManager nm = ClientModel.instance.getNetworkManager();
				nm.sendCommand(new ExitGameCommand());
				ClientWindow.getInstance().getSetupWindow().setScreen(new MainMenu());
				ClientWindow.getInstance().switchToSetup();
			}
		});

		draw(border, box_x, box_y);
	}

}
