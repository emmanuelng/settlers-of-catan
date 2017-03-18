package catan.settlers.client.view.game;

import java.util.HashMap;

import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;

import catan.settlers.client.model.ClientModel;
import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.handlers.Clickable;
import catan.settlers.server.model.map.Edge;

public class EdgeImage extends MinuetoRectangle implements Clickable {

	private static final HashMap<Edge, EdgeImage> edges = new HashMap<>();
	private static final HashMap<Edge, EdgeImage> selected_edges = new HashMap<>();

	/**
	 * This hash map is used to get the size of rotated rectangles
	 */
	private static final HashMap<Double, MinuetoRectangle> rotatedRectangles = new HashMap<>();

	public static EdgeImage getEdgeImage(Edge edge, double rotation, int x, int y, boolean isSelected) {
		HashMap<Edge, EdgeImage> list = isSelected ? selected_edges : edges;

		if (list.get(edge) == null) {
			MinuetoColor color = MinuetoColor.RED;

			if (edge.getOwner() == null && !isSelected) {
				color = edge.isMaritime() ? new MinuetoColor(170, 218, 255) : new MinuetoColor(230, 230, 230);
			} else if (edge.getOwner() != null) {
				String username = edge.getOwner().getUsername();
				color = ClientWindow.getInstance().getGameWindow().getColorByUsername(username);
			}

			list.put(edge, new EdgeImage(edge, rotation, x, y, color));
		}

		return list.get(edge);
	}

	private int relativeX;
	private int relativeY;
	private Edge edgeModel;

	private int rot_width;
	private int rot_height;

	private boolean selected;

	private EdgeImage(Edge edge, double rotation, int relativeX, int relativeY, MinuetoColor edgeMColor) {
		super(HexagonImage.HEXSIDE, 10, edgeMColor, true);

		MinuetoImage rotated = rotate(rotation);
		this.rot_width = rotated.getWidth();
		this.rot_height = rotated.getHeight();
		this.edgeModel = edge;

		this.relativeX = relativeX;
		this.relativeY = relativeY;

		this.selected = false;

	}

	@Override
	public boolean isClicked(int x, int y) {
		return x > relativeX && x < relativeX + getWidth() && y > relativeY + 100 && y < relativeY + 100 + getHeight();
	}

	@Override
	public void onclick() {
		selected = !selected;

		if (selected) {
			ClientModel.instance.getGameStateManager().setSelectedEdge(edgeModel);
		} else {
			ClientModel.instance.getGameStateManager().setSelectedEdge(null);
		}
	}

	@Override
	public String getName() {
		return "Edge" + edgeModel;
	}

	@Override
	public int getWidth() {
		return rot_width;
	}

	@Override
	public int getHeight() {
		return rot_height;
	}

	/**
	 * Get the dimensions of a rotated edge
	 */

	public static int getWidthByRotation(double rotation) {
		if (rotatedRectangles.get(rotation) == null) {
			MinuetoRectangle rotated = new MinuetoRectangle(HexagonImage.HEXSIDE, 10, MinuetoColor.BLACK, false);
			rotated.rotate(rotation);
			rotatedRectangles.put(rotation, rotated);
		}
		return rotatedRectangles.get(rotation).getWidth();
	}

	public static int getHeightByRotation(double rotation) {
		if (rotatedRectangles.get(rotation) == null) {
			MinuetoRectangle rotated = new MinuetoRectangle(HexagonImage.HEXSIDE, 10, MinuetoColor.BLACK, false);
			rotated.rotate(rotation);
			rotatedRectangles.put(rotation, rotated);
		}
		return rotatedRectangles.get(rotation).getHeight();
	}

}
