package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.GameStateManager;
import catan.settlers.client.model.NetworkManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.ClickListener;
import catan.settlers.network.server.commands.game.SelectEdgeFeedbackCommand;

public class SelectEdgeLayer extends ImageLayer {

	private static final MinuetoColor confirm_btn_color = new MinuetoColor(55, 200, 113);

	private boolean clear;
	private Button hexConfirmButton;

	public SelectEdgeLayer() {
		this.hexConfirmButton = new Button(this, "Confirm", confirm_btn_color, getConfirmListener());
	}

	@Override
	public void compose(GameStateManager gsm) {
		if (!gsm.getShowSelectEdgeLayer()) {
			if (clear) {
				ClientWindow.getInstance().getGameWindow().clearLayerClickables(this);
				clear();
				clear = false;
			}
			return;
		} else {
			clear = true;
		}

		draw(hexConfirmButton.getImage(), ClientWindow.WINDOW_WIDTH / 2 - hexConfirmButton.getImage().getWidth() / 2,
				ClientWindow.WINDOW_HEIGHT - hexConfirmButton.getImage().getHeight() - 20);
	}

	private ClickListener getConfirmListener() {
		return new ClickListener() {
			@Override
			public void onClick() {
				GameStateManager gsm = ClientModel.instance.getGameStateManager();
				NetworkManager nm = ClientModel.instance.getNetworkManager();

				nm.sendCommand(new SelectEdgeFeedbackCommand());
				gsm.setSelectedEdge(null);
				gsm.setdBox(null, null);
				gsm.doShowSelectHexLayer(false);
			}
		};
	}

}
