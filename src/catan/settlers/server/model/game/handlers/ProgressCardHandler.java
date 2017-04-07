package catan.settlers.server.model.game.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.network.client.commands.game.DiscardCardsCommand;
import catan.settlers.network.client.commands.game.FailureCommand;
import catan.settlers.network.client.commands.game.MoveRobberCommand;
import catan.settlers.network.client.commands.game.UpdateCardsCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.cards.CommercialHarborCommand;
import catan.settlers.network.client.commands.game.cards.InventorCommand;
import catan.settlers.network.client.commands.game.cards.MasterMerchantCommand;
import catan.settlers.network.client.commands.game.cards.SelectResourceCommand;
import catan.settlers.network.server.commands.game.cards.SelectResourceResponseCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.ProgressCards.ProgressCardType;
import catan.settlers.server.model.game.handlers.set.CommercialHarborSetHandler;
import catan.settlers.server.model.game.handlers.set.MasterMerchantSetHandler;
import catan.settlers.server.model.game.handlers.set.SetOfOpponentMove;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Village;

public class ProgressCardHandler {

	private Game game;
	private Player currentPlayer;

	public ProgressCardHandler(Game game) {
		this.game = game;
	}

	/**
	 * Progress card logic handling.
	 * 
	 * TODO: Implement the TODO methods. Once this is done, remove the TODO tag.
	 * You can go directly to the method using Ctrl + click on the method name.
	 * You can also get the method description by hovering on the method
	 * signature.
	 */
	public void handle(Player sender, ProgressCardType card) {
		switch (card) {
		case COMMERCIAL_HARBOR:
			commercialHarbor();
			break;
		case MASTER_MERCHANT:
			masterMerchant(sender); // TODO
			break;
		case MERCHANT:
			merchant(sender); // TODO
			break;
		case MERCHANT_FLEET:
			merchantFleet(sender); // TODO
			break;
		case RESOURCE_MONOPOLY:
			resourceMonopoly(sender); // TODO
			break;
		case TRADE_MONOPOLY:
			tradeMonopoly(sender); // TODO
			break;
		case BISHOP:
			bishop(sender); // TODO
			break;
		case CONSTITUTION:
			constitution(sender); // TODO
			break;
		case DESERTER:
			deserter(sender); // TODO
			break;
		case DIPLOMAT:
			diplomat(sender); // TODO
			break;
		case INTRIGUE:
			intrigue(sender); // TODO
			break;
		case SABOTEUR:
			saboteur(sender); // TODO
			break;
		case SPY:
			spy(sender); // TODO
			break;
		case WARLORD:
			warlord(sender);
			break;
		case WEDDING:
			wedding(sender); // TODO
			break;
		case CRANE:
			crane(sender);
			break;
		case ENGINEER:
			engineer(sender);
			break;
		case INVENTOR:
			inventor(sender); // TODO
			break;
		case IRRIGATION:
			irrigation(sender);
			break;
		case MEDICINE:
			medicine(sender); // TODO
			break;
		case MINING:
			mining(sender);
			break;
		case PRINTER:
			printer(sender); // TODO
			break;
		case ROAD_BUILDING:
			roadBuilding(sender); // TODO
			break;
		case SMITH:
			smithCard(sender); // TODO
			break;
		default:
			break;
		}
		game.getProgressCards().returnProgressCard(card);
	}

	/**
	 * give each opponent a resource of your choice in exchange for a commodity
	 * of their choice
	 */
	private void commercialHarbor() {
		// Build a set of opponent move
		Player currentPlayer = game.getCurrentPlayer();
		CommercialHarborSetHandler set = new CommercialHarborSetHandler(game, game.getCurrentPlayer());
		Player firstOpponent = null;
		for (Player p : game.getParticipants()) {
			if (p == game.getCurrentPlayer())
				continue;

			if (p.hasCommodities())
				set.waitForPlayer(p);

			if (firstOpponent == null)
				firstOpponent = p;
		}

		if (!set.isEmpty()) {
			game.setCurSetOfOpponentMove(set);

			// Send initial command
			String currentPlayerUsername = currentPlayer.getUsername();
			String firstOpponentUsername = firstOpponent == null ? "" : firstOpponent.getUsername();

			CommercialHarborCommand cmd = new CommercialHarborCommand(currentPlayerUsername, firstOpponentUsername);
			game.sendToAllPlayers(cmd);

			// Update cards
			currentPlayer.useProgressCard(ProgressCardType.COMMERCIAL_HARBOR);
			currentPlayer.sendCommand(new UpdateCardsCommand(currentPlayer.getProgressCards()));
		}
	}

	/**
	 * choose two cards to take from an opponent with more VPs than you
	 */
	private void masterMerchant(Player sender) {
		ArrayList<String> playersWithMoreVPs = new ArrayList<>();

		for (Player p : game.getParticipants()) {
			if (p != sender && p.getVP() > sender.getVP()) {
				if (p.getNbResourceCards() >= 2)
					playersWithMoreVPs.add(p.getUsername());
			}
		}

		if (!playersWithMoreVPs.isEmpty()) {
			MasterMerchantSetHandler set = new MasterMerchantSetHandler(game, playersWithMoreVPs);
			set.waitForPlayer(sender);
			game.setCurSetOfOpponentMove(set);

			game.sendToAllPlayers(new MasterMerchantCommand(playersWithMoreVPs, sender.getUsername()));

			// Update cards
			sender.useProgressCard(ProgressCardType.COMMERCIAL_HARBOR);
			sender.sendCommand(new UpdateCardsCommand(sender.getProgressCards()));
		}
	}

	/**
	 * place merchant on a tile; trade that resource at 2:1
	 * 
	 * @param sender
	 */
	private void merchant(Player sender) {

	}

	/**
	 * select a resource; you may trade that at 2:1 for this turn
	 */
	private void merchantFleet(Player sender) {
		sender.sendCommand(new SelectResourceCommand("merchant fleet"));
		ResourceType resource = sender.getCurrentSelectedResource();
		sender.setTradeAtAdvantage(resource);
	}

	/**
	 * name a resource; all other players must give you 2 of that if they have
	 */
	private void resourceMonopoly(Player sender) {
		currentPlayer.sendCommand(new SelectResourceCommand("Resource Monopoly- card player chooses resource to take"));
	}

	/**
	 * name a commodity; all players must give you 1 if that if they have it
	 */
	private void tradeMonopoly(Player sender) {
	}

	/**
	 * move the robber, but get a random card from EACH player on the robber's
	 * new hex
	 * 
	 * @param sender
	 */
	private void bishop(Player sender) {
		sender.sendCommand(new MoveRobberCommand(true));
	}

	/**
	 * one VP for player
	 */
	private void constitution(Player sender) {
		sender.incrementVP(1);
	}

	/**
	 * choose an opponent who must remove a knight; place a knight of equal
	 * strength
	 */
	private void deserter(Player sender) {

	}

	/**
	 * remove any open road; if it's your own, you may place it somewhere else
	 */
	private void diplomat(Player sender) {

	}

	/**
	 * choose an opponent's knight to displace for free, but it must be on your
	 * road network
	 */
	private void intrigue(Player sender) {

	}

	/**
	 * all players with more VPs than you must discard half their cards
	 * 
	 * @param sender
	 */
	private void saboteur(Player sender) {
		for (Player p : game.getParticipants()) {
			if (p.getVP() > sender.getVP()) {
				// Probably want some message letting them know saboteur was
				// played
				p.sendCommand(new DiscardCardsCommand());
			}
		}
	}

	/**
	 * steal a non-VP progress card from an opponent
	 */
	private void spy(Player sender) {

	}

	/**
	 * Activate all your knights for free
	 */
	private void warlord(Player sender) {
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
	}

	/**
	 * All players with more VPs than you must give you 2 resources of their
	 * choice
	 */
	private void wedding(Player sender) {
		ArrayList<Player> otherPlayers = game.getParticipants();
		for (Player p : otherPlayers) {
			if (p.getVP() > sender.getVP()) {
				p.sendCommand(new SelectResourceCommand("Wedding - all players with more VP than you give 2 resource"));
				HashMap<ResourceType, Integer> resourceToGive = p.getCurrentSelectedResources();
				for (ResourceType r : resourceToGive.keySet()) {
					p.removeResource(r, resourceToGive.get(r));
					sender.giveResource(r, resourceToGive.get(r));
				}
			}
		}
	}

	/**
	 * Your next city improvement you build this turn costs one less commodity
	 * than usual
	 */
	private void crane(Player sender) {
		sender.playCrane();
	}

	/**
	 * Your next city wall is free
	 */
	private void engineer(Player sender) {
		sender.playEngineer();
	}

	/**
	 * Swap the numbers of two hexagons on the board. They must not be 2, 6, 8,
	 * 12
	 */
	private void inventor(Player sender) {
		sender.sendCommand(new InventorCommand());
	}

	/**
	 * Draw two grain for each field tile you have at least one village on
	 */
	private void irrigation(Player sender) {
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
							sender.sendCommand(new UpdateResourcesCommand(currentPlayer.getResources()));
						}
					}
				}
			}
		}
	}

	/**
	 * Your next city upgrade this turn costs 2 ore and 1 grain
	 */
	private void medicine(Player sender) {
		sender.playMedicine();
	}

	/**
	 * Draw two ore for each mountain tile you have at least one village on
	 */
	private void mining(Player sender) {
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
							sender.sendCommand(new UpdateResourcesCommand(currentPlayer.getResources()));
						}
					}
				}
			}
		}
	}

	/**
	 * Get one VP!
	 */
	private void printer(Player sender) {
		sender.incrementVP(1);
	}

	/**
	 * Build two roads at no cost
	 */
	private void roadBuilding(Player sender) {
		sender.playRoadBuilding();
	}

	/**
	 * Upgrade two knights at no cost
	 */
	private void smithCard(Player sender) {
		sender.playSmith();
	}
}
