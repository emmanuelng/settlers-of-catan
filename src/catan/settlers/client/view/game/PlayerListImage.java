package catan.settlers.client.view.game;

import java.util.ArrayList;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.ImageFileManager;
import catan.settlers.client.view.ClientWindow;

public class PlayerListImage extends MinuetoImage {

	public PlayerListImage() {
		super(ClientWindow.WINDOW_WIDTH, ClientWindow.WINDOW_HEIGHT);
		compose();
	}

	public void compose() {
		ArrayList<String> participants = ClientModel.instance.getGameStateManager().getParticipants();
		int offsetX = 30, offsetY = 100, spaceBetweenPlayers = 100;

		if (participants == null)
			return;

		if (participants != null && ClientModel.instance.getGameStateManager().getCurrentPlayer() != null) {
			MinuetoFont unameFont = new MinuetoFont("arial", 20, true, false);
			MinuetoFont vpFont = new MinuetoFont("arial", 15, false, false);

			for (int i = 0; i < participants.size(); i++) {
				ImageFileManager ifm = ClientModel.instance.getImageFileManager();
				MinuetoImageFile logo = ifm.load("images/logo_" + (i + 1) + ".png");

				boolean isCurrentPlayer = ClientModel.instance.getGameStateManager().getCurrentPlayer()
						.equals(participants.get(i));
				String userStr = isCurrentPlayer ? "> " + participants.get(i) : participants.get(i);
				MinuetoText username = new MinuetoText(userStr, unameFont, MinuetoColor.BLACK, true);

				// TODO: Update the victory points
				MinuetoText vps = new MinuetoText("0 victory points", vpFont, MinuetoColor.BLACK, true);

				draw(logo, offsetX, offsetY + i * spaceBetweenPlayers);
				draw(username, logo.getWidth() + 10 + offsetX, offsetY + i * spaceBetweenPlayers);
				draw(vps, logo.getWidth() + 10 + offsetX, username.getHeight() + 8 + offsetY + i * spaceBetweenPlayers);
			}
		}
	}
}