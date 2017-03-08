package catan.settlers.server.model;

import java.io.Serializable;

import catan.settlers.server.model.map.Hexagon.TerrainType;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	
	
	
	/*
	 *  Non-progress cards (ie: resources and commodities) will be stored in a simple
	 *  integer array. Each value in the array corresponds to the amount of each type
	 *  of card possessed by the player. The convention for card types is alphabetical,
	 *  resource before commodities:
	 *  [0] BRICK, [1] GRAIN, [2] LUMBER, [3] ORE, [4] WOOL, 
	 *  [5] CLOTH, [6] COIN, [7] PAPER
	 */
	private int[] resources;
	
	public Player(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean comparePassword(String proposedPassword) {
		return password.equals(proposedPassword);
	}
	
	public void giveResource(TerrainType resource, int amount) {
		int i = terrainToInt(resource);
		if (i >= 0) {
			resources[i] += amount;
		}
	}
	public void giveCityResource(TerrainType resource, int amount) {
		int i = cityTerrainToInt(resource);
		if (i >= 0) {
			resources[i] += amount;
		}
	}
	
	public void takeResource(TerrainType resource, int amount) {
		int i = terrainToInt(resource);
		if ((i >= 0) && (resources[i] >= amount)) {
			resources[i] -= amount;
		}
	}
	
	public void maritimeTrade(TerrainType resourceToGet, TerrainType resourceToGive) {
		int give = terrainToInt(resourceToGive);
		int get = terrainToInt(resourceToGet);
		if (resources[give] >= 4 && give >= 0 && get >= 0) {
			resources[give] -= 4;
			resources[get]++;
		}
	}
	
	private int terrainToInt(TerrainType t) {
		switch (t) {
		case PASTURE: return 4;
		case FOREST: return 2;
		case MOUNTAIN: return 3;
		case HILLS: return 0;
		case FIELD: return 1;
		case GOLDMINE: return 6;
		default: return -1;
		}
	}
	
	private int cityTerrainToInt(TerrainType t) {
		switch (t) {
		case PASTURE: return 5;
		case FOREST: return 7;
		case MOUNTAIN: return 6;
		case HILLS: return 0;
		case FIELD: return 1;
		case GOLDMINE: return 6;
		default: return -1;
		}
	}
}