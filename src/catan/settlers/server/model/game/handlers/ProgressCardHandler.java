package catan.settlers.server.model.game.handlers;

import java.util.ArrayList;

import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.ProgressCards.ProgressCardType;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Village;

public class ProgressCardHandler {

	private Game game;
	private ArrayList<Player> participants;
	private Player currentPlayer;
	
	public ProgressCardHandler(Game game) {
		this.game = game;
	}
	
	public void handle(Player sender, ProgressCardType card) {
		switch (card) {
		case COMMERCIAL_HARBOR:
			// give each opponent a resource of your choice in exchange for a commodity of their choice
			break;
		case MASTER_MERCHANT:
			// choose two cards to take from an opponent with more VPs than you
			break;
		case MERCHANT:
			// place merchant on a tile; trade that resource at 2:1
			break;
		case MERCHANT_FLEET:
			// select a resource; you may trade that at 2:1 for this turn
			break;
		case RESOURCE_MONOPOLY:
			// name a resource; all other players must give you 2 of that if they have
			break;
		case TRADE_MONOPOLY:
			// name a commodity; all players must give you 1 if that if they have it
			break;
		case BISHOP:
			// move the robber, but get a random card from EACH player on the robber's new hex
			break;
		case CONSTITUTION:
			// one VP for player
			break;
		case DESERTER:
			// choose an opponent who must remove a knight; place a knight of equal strength
			break;
		case DIPLOMAT:
			// remove any open road; if it's your own, you may place it somewhere else
			break;
		case INTRIGUE:
			// choose an opponent's knight to displace for free, but it must be on your road network
			break;
		case SABOTEUR:
			// all players with more VPs than you must discard half their cards
			break;
		case SPY:
			// steal a non-VP progress card from an opponent
			break;
		case WARLORD:
			// Activate all your knights for freeeeee
			ArrayList<Intersection> intersections = game.getGameBoardManager().getBoard().getIntersections();
			for (Intersection i : intersections) {
				IntersectionUnit u = i.getUnit();
				if (u instanceof Knight) {
					Knight knight = (Knight) u;
					if (knight.getOwner() == sender) {
						knight.activateKnight();
					}
				}
			}
			break;
		case WEDDING:
			// All players with more VPs than you must give you 2 resources of their choice
			break;
		case CRANE:
			// Your next city improvement you build this turn costs one less commodity than usual
			sender.playCrane();
			break;
		case ENGINEER:
			// Your next city wall is free
			sender.playEngineer();
			break;
		case INVENTOR:
			// Swap the numbers of two hexagons on the board. They must not be 2, 6, 8, 12
			break;
		case IRRIGATION:
			// Draw two grain for each field tile you have at least one village on
			for (int y = 0; y < 7; y++) {
				for (int x = 0; x < 7; x++) {
					Hexagon h = game.getGameBoardManager().getBoard().getHexagonAt(x, y);
					if (h != null) {
						if (h.getType() == TerrainType.FIELD) {
							boolean onHex = false;
							for (IntersectionLoc loc : IntersectionLoc.values()) {
								IntersectionUnit u = h.getIntersection(loc).getUnit();
								if (u instanceof Village) {
									if (u.getOwner() == sender) {
										onHex = true;
										break;
									}
								}
							}
							if (onHex) {
								sender.giveResource(ResourceType.GRAIN, 2);
							}
						}
					}
				}
			}
			break;
		case MEDICINE:
			// Your next city upgrade this turn costs 2 ore and 1 grain
			sender.playMedicine();
			break;
		case MINING:
			// Draw two ore for each mountain tile you have at least one village on
			for (int y = 0; y < 7; y++) {
				for (int x = 0; x < 7; x++) {
					Hexagon h = game.getGameBoardManager().getBoard().getHexagonAt(x, y);
					if (h != null) {
						if (h.getType() == TerrainType.MOUNTAIN) {
							boolean onHex = false;
							for (IntersectionLoc loc : IntersectionLoc.values()) {
								IntersectionUnit u = h.getIntersection(loc).getUnit();
								if (u instanceof Village) {
									if (u.getOwner() == sender) {
										onHex = true;
										break;
									}
								}
							}
							if (onHex) {
								sender.giveResource(ResourceType.ORE, 2);
							}
						}
					}
				}
			}
			break;
		case PRINTER:
			// Get one VP!
			break;
		case ROAD_BUILDING:
			// Build two roads at no cost
			sender.playRoadBuilding();
			break;
		case SMITH:
			// Upgrade two knights at no cost
			sender.playSmith();
			break;
		}
	}
}
