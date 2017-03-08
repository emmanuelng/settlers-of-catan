package catan.settlers.server.model.map;

import java.io.Serializable;

import catan.settlers.server.model.units.IntersectionUnit;

public class Intersection implements Serializable {

	private static final long serialVersionUID = 1L;
	private IntersectionUnit unit;
	
	public Intersection() {
		unit = null;
	}

	public IntersectionUnit getUnit() {
		return unit;
	}
	
	public void setUnit(IntersectionUnit unit) {
		this.unit = unit;
	}

}
