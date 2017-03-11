package catan.settlers.server.model;

import java.io.Serializable;

import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Hexagon.TerrainType;
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
									p.giveResource(terrainToResource(hex.getType())[0], 1);
								case CITY:
									p.giveResource(terrainToResource(hex.getType())[0], 1);
									p.giveResource(terrainToResource(hex.getType())[1], 1);
								case METROPOLIS:
									p.giveResource(terrainToResource(hex.getType())[0], 1);
									p.giveResource(terrainToResource(hex.getType())[1], 1);
								}
							}
						}
					}
				}
			}
		}
	}

	private ResourceType[] terrainToResource(TerrainType t) {
		switch (t) {
		case PASTURE:
			return new ResourceType[] { ResourceType.WOOL, ResourceType.CLOTH };
		case FOREST:
			return new ResourceType[] { ResourceType.LUMBER, ResourceType.PAPER };
		case MOUNTAIN:
			return new ResourceType[] { ResourceType.ORE, ResourceType.COIN };
		case HILLS:
			return new ResourceType[] { ResourceType.BRICK, ResourceType.BRICK };
		case FIELD:
			return new ResourceType[] { ResourceType.GRAIN, ResourceType.GRAIN };
		case GOLDMINE:
			return new ResourceType[] { ResourceType.COIN, ResourceType.COIN };
		default:
			return null;
		}

	}
}
