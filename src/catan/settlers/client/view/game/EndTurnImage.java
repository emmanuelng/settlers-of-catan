package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.ImageFileManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.Clickable;
import catan.settlers.network.server.commands.game.EndTurnCommand;

public class EndTurnImage extends MinuetoImage implements Clickable {
	private int relativeX;
	private int relativeY;

	public EndTurnImage(int relativeX, int relativeY) {
		super(51, 60);

		this.relativeX = relativeX;
		this.relativeY = relativeY;

		ImageFileManager ifm = ClientModel.instance.getImageFileManager();
		draw(ifm.load("images/endturn-new.png"), 0, 0);
		draw(new MinuetoText("End Turn", new MinuetoFont("arial", 10, false, false), MinuetoColor.BLACK), 0, 50);
	}

	@Override
	public boolean isClicked(int x, int y) {
		// TODO: absolute coordinates are hard coded for now
		return x > relativeX && x < relativeX + getWidth() && y > relativeY && y < relativeY + getHeight();
	}

	@Override
	public void onclick() {
		ClientModel.instance.getNetworkManager().sendCommand(new EndTurnCommand());
		
		// Reset the client model
		ClientModel.instance.getGameStateManager().setSelectedEdge(null);
		ClientModel.instance.getGameStateManager().setSelectedIntersection(null);
		ClientWindow.getInstance().getGameWindow().setDialogBox(null);
	}

	@Override
	public String getName() {
		return "End Turn";
	}

}
