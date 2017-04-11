package catan.settlers.server.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Village;

public class GameBoardManager implements Serializable {

	private static final long serialVersionUID = -2561852050287131049L;
	private GameBoard board;

	public GameBoardManager() {
		this.board = new GameBoard();
	}

	public GameBoard getBoard() {
		return board;
	}

	public GameBoard getBoardDeepCopy() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(board);
			out.flush();
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bis);
			GameBoard newBoard = (GameBoard) in.readObject();
			out.close();
			in.close();
			return newBoard;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Produces the resources for all the settlements and cities on the board.
	 * Returns a list of all the players who received resources.
	 */
	public HashSet<Player> produceResources(int diceValue) {
		HashSet<Player> playersWhoDrew = new HashSet<>();

		for (int x = 0; x < board.getLength(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				Hexagon hex = board.getHexagonAt(x, y);
				if (hex != null) {
					if (hex.getNumber() == diceValue && hex != board.getRobberHex()) {
						for (IntersectionLoc loc : IntersectionLoc.values()) {
							IntersectionUnit u = hex.getIntersection(loc).getUnit();
							if (u instanceof Village) {
								Player p = u.getOwner();
								playersWhoDrew.add(p);
								switch (((Village) u).getKind()) {
								case SETTLEMENT:
									p.giveResource(terrainToResource(hex.getType())[0], 1);
									break;
								case CITY:
									p.giveResource(terrainToResource(hex.getType())[0], 1);
									p.giveResource(terrainToResource(hex.getType())[1], 1);
									break;
								case METROPOLIS:
									p.giveResource(terrainToResource(hex.getType())[0], 1);
									p.giveResource(terrainToResource(hex.getType())[1], 1);
									break;
								}
							}
						}
					} else if (hex.getType() == TerrainType.FISHINGGROUND && hex.getNumber() == diceValue) {
						for (IntersectionLoc loc : IntersectionLoc.values()) {
							IntersectionUnit u = hex.getIntersection(loc).getUnit();
							if (u instanceof Village) {
								Player p = u.getOwner();
								goFish(p);
							}
						}
					} else if (hex.getType() == TerrainType.LAKE && (diceValue <= 2 || diceValue >= 11)) {
						for (IntersectionLoc loc : IntersectionLoc.values()) {
							IntersectionUnit u = hex.getIntersection(loc).getUnit();
							if (u instanceof Village) {
								Player p = u.getOwner();
								goFish(p);
							}
						}
					}
				}
			}
		}

		return playersWhoDrew;
	}

	public void goFish(Player p) {
		int n = (int) Math.ceil(Math.random() * (29 + board.bootDrawn()));
		if (n <= 11) {
			p.giveFish(1);
		} else if (n <= 21) {
			p.giveFish(2);
		} else if (n <= 29) {
			p.giveFish(3);
			;
		} else {
			p.giveBoot();
			board.drewBoot();
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
