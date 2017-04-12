package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.server.model.map.Hexagon;

public class InfoBoxLayer extends ImageLayer {

	private static final MinuetoFont titleFont = new MinuetoFont("arial", 18, true, false);
	private static final MinuetoFont descFont = new MinuetoFont("arial", 15, false, false);

	@Override
	public void compose(GameStateManager gsm) {
		if (gsm.getSelectedHex() == null) {
			clear();
			return;
		}

		clear();
		Hexagon hex = gsm.getSelectedHex();
		MinuetoText hexName = new MinuetoText(hex.getType().toString(), titleFont, MinuetoColor.BLACK);
		String resName = Hexagon.terrainToResource(hex.getType()) == null ? "Nothing"
				: Hexagon.terrainToResource(hex.getType()).toString();
		String descrStr = "Produces: " + resName;
		MinuetoText descr = new MinuetoText(descrStr, descFont, MinuetoColor.BLACK);

		int x = 30;
		int y = ClientWindow.WINDOW_HEIGHT - hexName.getHeight() - 10 - descr.getHeight() - 20;

		draw(hexName, x, y);
		y += hexName.getHeight() + 10;
		draw(descr, x, y);
	}

}
