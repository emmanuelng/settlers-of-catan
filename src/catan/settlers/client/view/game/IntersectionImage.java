package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.Clickable;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Village;

public class IntersectionImage extends MinuetoImage implements Clickable {

	private static final HashMap<Intersection, IntersectionImage> intersections = new HashMap<>();
	private static final HashMap<Intersection, IntersectionImage> selected_intersections = new HashMap<>();

	public static IntersectionImage getIntersectionImage(Intersection intersection, int x, int y, boolean isSelected) {
		HashMap<Intersection, IntersectionImage> list = isSelected ? selected_intersections : intersections;

		if (list.get(intersection) == null)
			list.put(intersection, new IntersectionImage(intersection, x, y, isSelected));

		return list.get(intersection);
	}

	private int relativeX;
	private int relativeY;
	private Intersection intersectionModel;

	private IntersectionImage(Intersection intersection, int relativeX, int relativeY, boolean isSelected) {
		super(20, 20);

		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.intersectionModel = intersection;

		if (intersection.getUnit() == null) {
			if (!isSelected) {
				if (intersectionModel.isMaritime()) {
					drawCircle(new MinuetoColor(182, 215, 255), 0, 0, 20);
				} else {
					drawCircle(new MinuetoColor(204, 204, 204), 0, 0, 20);
				}
			} else {
				drawCircle(MinuetoColor.RED, 0, 0, 20);
			}
		} else {
			if (intersection.getUnit() instanceof Village) {
				Village village = (Village) intersection.getUnit();
				try {
					switch (village.getKind()) {
					case SETTLEMENT:
						draw(new MinuetoImageFile("images/building.png"), 0, 0);
						break;
					case CITY:
						int playerNo = ClientWindow.getInstance().getGameWindow()
								.getPlayerNumber(village.getOwner().getUsername());
						draw(new MinuetoImageFile("images/city" + playerNo + ".png"), 0, 0);
						break;
					default:
						break;
					}
				} catch (Exception e) {
					// TODO: Failed to load images
				}
			}
		}
	}

	@Override
	public boolean isClicked(int x, int y) {
		return x > relativeX && x < relativeX + getWidth() && y > relativeY + 100 && y < relativeY + 100 + getHeight();
	}

	@Override
	public void onclick() {
		if (intersectionModel != ClientModel.instance.getGameStateManager().getSelectedIntersection()) {
			ClientModel.instance.getGameStateManager().setSelectedIntersection(intersectionModel);
		} else {
			ClientModel.instance.getGameStateManager().setSelectedIntersection(null);
		}

	}

	@Override
	public String getName() {
		return "Intersection" + intersectionModel;
	}

}
