package catan.settlers.server.model;

import java.io.Serializable;

import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Village;

public class GameBoardManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private GameBoard board;

	public GameBoardManager() {
		this.board = new GameBoard();
	}

	public GameBoard getBoard() {
		return board;
	}
	
	// Distributes resources to players for a roll of i
	public void drawForRoll(int i) {
		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				Hexagon hex = board.getHexagonAt(x, y);
				if (hex != null) {
					if (hex.getNumber() == i) {
						for (IntersectionLoc loc : IntersectionLoc.values()) {
							IntersectionUnit u = hex.getIntersection(loc).getUnit();
							if (u instanceof Village) {
								Player p = u.getOwner();
								switch (((Village) u).getKind()) {
								case SETTLEMENT: 
									p.giveResource(hex.getType(), 1);
								case CITY:
									p.giveResource(hex.getType(), 1);
									p.giveCityResource(hex.getType(), 1);
								case METROPOLIS:
									p.giveResource(hex.getType(), 1);
									p.giveCityResource(hex.getType(), 1);
								}
							}
						}
					}
				}
			}
		}
	}
}
