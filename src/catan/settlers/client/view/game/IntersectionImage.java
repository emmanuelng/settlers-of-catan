package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.Clickable;
import catan.settlers.server.model.map.Intersection;

public class IntersectionImage extends MinuetoImage implements Clickable {

	private int relativeX;
	private int relativeY;
	private Intersection intersectionModel;

	public IntersectionImage(int relativeX, int relativeY, Intersection intersection) {
		super(20, 20);

		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.intersectionModel = intersection;

		if (intersection != ClientModel.instance.getCurrentIntersection()) {
			drawCircle(new MinuetoColor(204, 204, 204), 0, 0, 20);
		} else if (intersection == ClientModel.instance.getCurrentIntersection()) {
			drawCircle(MinuetoColor.RED, 0, 0, 20);

		} else if (intersection.getUnit().isVillage()) {
			MinuetoImage settlement;
			try {
				settlement = new MinuetoImageFile("images/building.png");
				draw(settlement, 0, 0);
			} catch (MinuetoFileException e) {
				System.out.println("couldnt load");
				return;
			}
		}
	}

	@Override
	public boolean isClicked(int x, int y) {
		return x > relativeX && x < relativeX + getWidth() && y > relativeY + 100 && y < relativeY + 100 + getHeight();
	}

	@Override
	public void onclick() {
		System.out.println("Intersection was clicked!");
		DialogBox dbox = new DialogBox("Build Settlement Here?", "Click on end turn to confirm");
		if (intersectionModel != ClientModel.instance.getCurrentIntersection()) {
			ClientModel.instance.setCurrentIntersection(intersectionModel);
			ClientWindow.getInstance().getGameWindow().setDialogBox(dbox);

		} else {
			ClientModel.instance.setCurrentIntersection(null);
			ClientWindow.getInstance().getGameWindow().setDialogBox(null);

		}
	}

	@Override
	public String getName() {
		return "Intersection" + intersectionModel;
	}

}
