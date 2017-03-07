package catan.settlers.server.model.map;

import java.io.Serializable;

public class Tuple implements Serializable {

	private static final long serialVersionUID = 1L;
	public int x;
	public int y;

	public Tuple(int a, int b) {
		x = a;
		y = b;
	}
}
