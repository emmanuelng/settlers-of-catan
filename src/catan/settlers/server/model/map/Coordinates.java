package catan.settlers.server.model.map;

import java.io.Serializable;

import catan.settlers.server.model.map.Hexagon.Direction;

public class Coordinates implements Serializable {

	private static final long serialVersionUID = 1L;
	private int x;
	private int y;

	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/**
	 * Computes the offset coordinates of the neighbor located at the given
	 * direction
	 * 
	 * @param dir
	 *            The direction of the neighbor
	 * @return The new coordinates. Be careful, no validation is made. These
	 *         coordinate might be invalid depending on the application
	 */
	public Coordinates getCoordsInDir(Direction dir) {
		int new_x = -1, new_y = -1;
		switch (dir) {
		case WEST:
			new_x = x - 1;
			new_y = y;
			break;
		case NORTHWEST:
			new_x = x - 1;
			new_y = y - 1;
			break;
		case NORTHEAST:
			new_x = x;
			new_y = y - 1;
			break;
		case EAST:
			new_x = x + 1;
			new_y = y;
			break;
		case SOUTHEAST:
			new_x = x;
			new_y = y + 1;
			break;
		case SOUTHWEST:
			new_x = x - 1;
			new_y = y + 1;
			break;
		default:
			break;
		}

		return new Coordinates(new_x, new_y);
	}
}
