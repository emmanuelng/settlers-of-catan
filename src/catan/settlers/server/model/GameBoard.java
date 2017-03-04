package catan.settlers.server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameBoard {
	private Hex[][] hexes;

	public GameBoard() {
		// Populate hex array with new hexes, provided they are within bounds of game board
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				if (Math.abs(x-y) < 3) {
					hexes[x][y] = new Hex();
				} else {
					hexes[x][y] = null;
				}
				// As we create hexes, we populate their edges at the same time
				// If a neighboring hex already exists, use its edge; create otherwise
				for (int dx = -1; dx <= 1; dx++) {
					for (int dy = -1; dy <= 1; dy++) {
						if (dx + dy != 0) {
							Hex h = getNeighbour(x, y, dx, dy);
							int idx = offsetToIndex(dx,dy);
							if (h != null) {
								hexes[x][y].setEdge(h.getEdge((idx+3)%6), idx);
								if (hexes[x][y].getIntersection(idx) == null) {
									hexes[x][y].setIntersection(h.getIntersection((idx+3)%6), idx);
								}
								if (hexes[x][y].getIntersection((idx+5)%6) == null) {
									hexes[x][y].setIntersection(h.getIntersection((idx+2)%6), (idx+5)%6);
								}
							} else {
								hexes[x][y].setEdge(new Edge(), idx);
								
								// If BOTH neighbors sharing the intersection are null, we create one
								Tuple t = indexToOffset((idx+1)%6);
								if (getNeighbour(x, y, t.x, t.y) == null) {
									hexes[x][y].setIntersection(new Intersection(), idx);
								}
							}
						}
					}
				}
			}
		}
		populateEdgesAndIntersections();
		randomizeHexes();
	}
	
	private void populateEdgesAndIntersections() {
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				if (Math.abs(x-y) < 3) {
					Hex h = hexes[x][y];
					for (int i = 0; i < 6; i++) {
						Edge e = h.getEdge(i);
						if (i < 3) {
							e.addLeftEdge(h.getEdge((i+5)%6));
							e.addRightEdge(h.getEdge((i+1)%6));
						} else {
							e.addRightEdge(h.getEdge((i+5)%6));
							e.addLeftEdge(h.getEdge((i+1)%6));
						}
						
						Intersection t = h.getIntersection(i);
						t.addEdge(e);
						t.addEdge(h.getEdge((i+1)%6));
						t.addIntersection (h.getIntersection((i+5)%6));
						t.addIntersection (h.getIntersection((i+1)%6));
						
						if (e.getIntersection(0) == null || e.getIntersection(1) == null) {
							e.setIntersection(h.getIntersection(i), 0);
							e.setIntersection(h.getIntersection((i+5)%6), 1);
						}
					}
				}
			}
		}	
	}
	
	private void randomizeHexes() {
		ArrayList<TerrainType> terrainPool = new ArrayList<>();
		terrainPool.add(TerrainType.HILLS); 	terrainPool.add(TerrainType.HILLS);		terrainPool.add(TerrainType.HILLS);
		terrainPool.add(TerrainType.MOUNTAIN); 	terrainPool.add(TerrainType.MOUNTAIN); 	terrainPool.add(TerrainType.MOUNTAIN);
		terrainPool.add(TerrainType.PASTURE); 	terrainPool.add(TerrainType.PASTURE); 	terrainPool.add(TerrainType.PASTURE); 	terrainPool.add(TerrainType.PASTURE);
		terrainPool.add(TerrainType.FIELD); 	terrainPool.add(TerrainType.FIELD); 	terrainPool.add(TerrainType.FIELD); 	terrainPool.add(TerrainType.FIELD);
		terrainPool.add(TerrainType.FOREST); 	terrainPool.add(TerrainType.FOREST); 	terrainPool.add(TerrainType.FOREST); 	terrainPool.add(TerrainType.FOREST);
		terrainPool.add(TerrainType.DESERT);	
		Collections.shuffle(terrainPool);
		
		ArrayList<Integer> diceValues = new ArrayList<>();
		diceValues.addAll(Arrays.asList(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
		Collections.shuffle(diceValues);
		
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				if (Math.abs(x-y) < 3) {
					if (terrainPool.get(0) != TerrainType.DESERT) {
						hexes[x][y].setNum(diceValues.remove(0));
					}
					hexes[x][y].setType(terrainPool.remove(0));
				}
			}
		}
	}
	
	// Utility functions for constructing the board.
	private int offsetToIndex(int dx, int dy) {
		if 			(dx ==  1 && dy ==  1) { return 0; } 
		else if 	(dx ==  1 && dy ==  0) { return 1; } 
		else if 	(dx ==  0 && dy == -1) { return 2; } 
		else if 	(dx == -1 && dy == -1) { return 3; } 
		else if 	(dx == -1 && dy ==  0) { return 4; }
		else if 	(dx ==  0 && dy ==  1) { return 5; }
		else { return -1; }
	}
	private Tuple indexToOffset(int i) {
		if 		(i == 0) { return new Tuple(-1, -1); }
		else if (i == 1) { return new Tuple( 1,  0); }
		else if (i == 2) { return new Tuple( 0, -1); }
		else if (i == 3) { return new Tuple(-1, -1); }
		else if (i == 4) { return new Tuple(-1,  0); }
		else if (i == 5) { return new Tuple( 0,  1); }
		else { return new Tuple(0,0); }
	}
	
	// Dodging out of bounds errors on hexes[][]
	private Hex getNeighbour(int x, int y, int dx, int dy) {
		
		if ( (x+dx > 4) || (x+dx < 0) || (y+dy > 4) || (y+dy < 0)) {
			return null;
		} else {
			return hexes[x+dx][y+dy];
		} 
	}
	
	
	/*
	 *  Distributes cards to players based on n being rolled.
	 *  Checks all hexes for n; if found, distributes resources and commodities
	 *  to all players with a village on that hex.
	 */
	
	public void drawNum(int n) {
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				if (Math.abs(x-y) < 3) {
					if (hexes[x][y].getNum() == n) {
						for (int i = 0; i < 6; i++) {
							IntersectionUnit u = hexes[x][y].getIntersection(i).getUnit();
							if (u instanceof Village) {	
								switch (((Village) u).getKind()) {
									case SETTLEMENT: 
										u.getOwner().giveResource(hexes[x][y].getResource(), 1);
									case CITY:
										u.getOwner().giveResource(hexes[x][y].getResource(), 1);
										u.getOwner().giveResource(hexes[x][y].getCommodity(), 1);
									case METROPOLIS:
										u.getOwner().giveResource(hexes[x][y].getResource(), 1);
										u.getOwner().giveResource(hexes[x][y].getCommodity(), 1);
								}
							}
							
						}
					}
				}
			}
		}	
	}
	
	
	public void placeSettlement(Player p, Intersection i) {
		if (i.canBuild()) {
			Village v = new Village(p);
			i.setUnit(v);
		}
	}
	
	public Hex[][] getHexes() {
		return hexes;
	}
}
