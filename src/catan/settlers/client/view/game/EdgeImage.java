package catan.settlers.client.view.game;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoRectangle;

import catan.settlers.client.model.ClientModel;
import catan.settlers.server.model.map.Edge;

public class EdgeImage extends MinuetoRectangle implements Clickable {

	private int relativeX;
	private int relativeY;
	private Edge edgeModel;

	private int rot_width;
	private int rot_height;
	private MinuetoColor edgeMColor;

	private boolean selected;

	public EdgeImage(Edge edge, double rotation, int relativeX, int relativeY, MinuetoColor edgeMColor) {
		super(40, 10, edgeMColor, true);

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
			ClientModel.instance.setCurrentEdge(edgeModel);
		} else {
			ClientModel.instance.setCurrentEdge(null);
		}
	}

	@Override
	public String getName() {
		return "Edge";
	}

	@Override
	public int getWidth() {
		return rot_width;
	}

	@Override
	public int getHeight() {
		return rot_height;
	}

}
