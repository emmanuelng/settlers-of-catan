package catan.settlers.client.view.game;

import org.minueto.window.MinuetoFrame;

import catan.settlers.client.model.ClientModel;

public class GameWindow extends MinuetoFrame {

	public GameWindow() {
		super(ClientModel.WINDOW_WIDTH, ClientModel.WINDOW_HEIGHT, true);
	}
}
