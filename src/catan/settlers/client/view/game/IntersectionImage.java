package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.model.ImageFileManager;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Village;
import catan.settlers.server.model.units.Port.PortKind;

public class IntersectionImage extends MinuetoImage {

	public static final int SIZE = 20;

	private static final HashMap<Intersection, IntersectionImage> intersections = new HashMap<>();
	private static final HashMap<Intersection, IntersectionImage> selected_intersections = new HashMap<>();

	public static IntersectionImage getIntersectionImage(Intersection intersection, boolean isSelected) {
		HashMap<Intersection, IntersectionImage> list = isSelected ? selected_intersections : intersections;

		if (list.get(intersection) == null)
			list.put(intersection, new IntersectionImage(intersection, isSelected));

		return list.get(intersection);
	}

	private Intersection intersectionModel;
	private boolean selected;

	private IntersectionImage(Intersection intersection, boolean selected) {
		super(SIZE, SIZE);

		this.intersectionModel = intersection;
		this.selected = selected;

		if (intersection.getUnit() == null) {
			if (!selected) {
				if (intersectionModel.isPortable()) {
					ImageFileManager ifm = ClientModel.instance.getImageFileManager();
					PortKind pkind = intersectionModel.getPortKind();
					MinuetoImage portImage = ifm.load("images/port_" + pkind + ".png");
					draw(portImage, 0, 0);
				} else if (intersectionModel.isMaritime()) {
					drawCircle(new MinuetoColor(182, 215, 255), 0, 0, 20);
				} else {
					drawCircle(new MinuetoColor(204, 204, 204), 0, 0, 20);
				}
			} else {
				drawCircle(MinuetoColor.RED, 0, 0, 20);
			}
		} else {
			GameWindow gw = ClientWindow.getInstance().getGameWindow();
			ImageFileManager imf = ClientModel.instance.getImageFileManager();

			if (intersection.getUnit() instanceof Village) {
				Village village = (Village) intersection.getUnit();
				int playerNo = gw.getPlayerNumber(village.getOwner().getUsername());

				switch (village.getKind()) {
				case SETTLEMENT:
					draw(imf.load("images/settlement" + playerNo + ".png"), 0, 0);
					break;
				case CITY:
					draw(imf.load("images/city" + playerNo + ".png"), 0, 0);
					break;
				default:
					break;
				}
			}
		}
	}

	public Intersection getModel() {
		return intersectionModel;
	}

	public boolean isSelectedImage() {
		return selected;
	}

}
