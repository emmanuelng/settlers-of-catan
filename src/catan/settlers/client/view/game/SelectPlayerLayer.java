package catan.settlers.client.view.game;

import java.util.ArrayList;
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
import catan.settlers.network.server.commands.game.PlayerSelectedCommand;

public class SelectPlayerLayer extends ImageLayer {

	private static final HashMap<String, MinuetoImage> shields = new HashMap<>();

	private static final int WIDTH = 500, HEIGHT = 350;
	private static final MinuetoColor bg_color = new MinuetoColor(249, 249, 249);
	private static final MinuetoColor border_color = new MinuetoColor(179, 179, 179);
	private static final MinuetoFont title_font = new MinuetoFont("arial", 28, true, false);
	private static final MinuetoFont description_font = new MinuetoFont("arial", 17, false, false);
	private static final MinuetoColor select_btn_color = new MinuetoColor(55, 200, 113);

	private int box_x;
	private int box_y;
	private MinuetoRectangle background;
	private MinuetoRectangle border;
	private MinuetoText title;
	private MinuetoText description;
	private boolean clear;

	public SelectPlayerLayer() {
		this.box_x = ClientWindow.WINDOW_WIDTH / 2 - WIDTH / 2;
		this.box_y = ClientWindow.WINDOW_HEIGHT / 2 - HEIGHT / 2;

		String title = "Select a player";
		String description = "Please select a player below";

		this.background = new MinuetoRectangle(WIDTH, HEIGHT, bg_color, true);
		this.border = new MinuetoRectangle(WIDTH, HEIGHT, border_color, false);
		this.title = new MinuetoText(title, title_font, MinuetoColor.BLACK);
		this.description = new MinuetoText(description, description_font, MinuetoColor.BLACK);
	}

	@Override
	public void compose(GameStateManager gsm) {

		if (!gsm.doShowSelectPlayerMenu()) {
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

		int y_offset = box_y + 25;

		draw(title, box_x + WIDTH / 2 - title.getWidth() / 2, y_offset);
		y_offset += title.getHeight() + 10;

		draw(description, box_x + WIDTH / 2 - description.getWidth() / 2, y_offset);
		y_offset += description.getHeight() + 30;

		ArrayList<String> participants = gsm.getPlayersToShow();

		ImageFileManager imf = ClientModel.instance.getImageFileManager();
		int players_displayed = 0, player_box_width = WIDTH / (participants.size());
		for (int i = 0; i < participants.size(); i++) {
			String curParticipant = participants.get(i);

			if (curParticipant.equals(ClientModel.instance.getUsername())) {
				continue;
			}

			int player_box_x = box_x + players_displayed * player_box_width;
			int player_box_y_offset = y_offset;

			if (shields.get(curParticipant) == null) {
				int playerNo = ClientWindow.getInstance().getGameWindow().getPlayerNumber(curParticipant);
				shields.put(curParticipant, imf.load("images/logo_" + playerNo + ".png"));
			}
			MinuetoImage shield = shields.get(curParticipant);
			draw(shield, player_box_x + player_box_width / 2 - shield.getWidth() / 2, player_box_y_offset);
			player_box_y_offset += shield.getHeight() + 10;

			MinuetoText username = new MinuetoText(curParticipant, title_font, MinuetoColor.BLACK);
			draw(username, player_box_x + player_box_width / 2 - username.getWidth() / 2, player_box_y_offset);
			player_box_y_offset += username.getHeight() + 10;

			int vp = gsm.getVictoryPoints().get(participants.get(i));
			MinuetoText playerdescr = new MinuetoText(vp + " victory points", description_font, MinuetoColor.BLACK);
			draw(playerdescr, player_box_x + player_box_width / 2 - playerdescr.getWidth() / 2, player_box_y_offset);
			player_box_y_offset += playerdescr.getHeight() + 30;

			Button selectPlayerBtn = new Button(this, "Select", select_btn_color, new ClickListener() {
				@Override
				public void onClick() {
					System.out.println("Select " + curParticipant);
					NetworkManager nm = ClientModel.instance.getNetworkManager();
					nm.sendCommand(new PlayerSelectedCommand(curParticipant, gsm.getSelectionReason()));
				}

			});

			draw(selectPlayerBtn.getImage(),
					player_box_x + player_box_width / 2 - selectPlayerBtn.getImage().getWidth() / 2,
					player_box_y_offset);

			players_displayed++;
		}
	}

}
