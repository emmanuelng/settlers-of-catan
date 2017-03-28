package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.server.model.map.Edge;

public class EdgeImage {

	private static final HashMap<Edge, EdgeImage> edges = new HashMap<>();
	private static final HashMap<Edge, EdgeImage> selected_edges = new HashMap<>();

	public static EdgeImage getEdgeImage(Edge edge, double rotation, boolean isSelected) {
		HashMap<Edge, EdgeImage> list = isSelected ? selected_edges : edges;

		if (list.get(edge) == null) {
			MinuetoColor color = MinuetoColor.RED;

			if (edge.getOwner() == null && !isSelected) {
				color = edge.isMaritime() ? new MinuetoColor(170, 218, 255) : new MinuetoColor(230, 230, 230);
			} else if (edge.getOwner() != null) {
				String username = edge.getOwner().getUsername();
				color = ClientWindow.getInstance().getGameWindow().getColorByUsername(username);
			}

			list.put(edge, new EdgeImage(edge, color, rotation, isSelected));
		}

		return list.get(edge);
	}

	private Edge edgeModel;
	private MinuetoImage image;
	private double rotation;
	private MinuetoColor color;
	private boolean selected;

	private EdgeImage(Edge edge, MinuetoColor color, double rotation, boolean selected) {
		this.edgeModel = edge;
		this.color = color;
		this.rotation = rotation;
		this.selected = selected;

	}

	public Edge getModel() {
		return edgeModel;
	}
	
	public MinuetoImage getImage() {
		if (image == null) {
			image = new MinuetoRectangle(HexagonImage.HEXSIDE, 10, color, true);
			image = image.rotate(rotation);
		}
		return image;
	}

	public boolean isSelectedImage() {
		return selected;
	}

}
