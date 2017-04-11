package catan.settlers.server.model.game.handlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.client.model.GameStateManager.SelectionReason;
import catan.settlers.network.client.commands.game.DiscardCardsCommand;
import catan.settlers.network.client.commands.game.MoveRobberCommand;
import catan.settlers.network.client.commands.game.SelectPlayerCommand;
import catan.settlers.network.client.commands.game.UpdateCardsCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.UpdateVPCommand;
import catan.settlers.network.client.commands.game.cards.BishopCommand;
import catan.settlers.network.client.commands.game.cards.CommercialHarborCommand;
import catan.settlers.network.client.commands.game.cards.InventorCommand;
import catan.settlers.network.client.commands.game.cards.MasterMerchantCommand;
import catan.settlers.network.client.commands.game.cards.MerchantCommand;
import catan.settlers.network.client.commands.game.cards.MerchantFleetCommand;
import catan.settlers.network.client.commands.game.cards.ResourceMonopolyCommand;
import catan.settlers.network.client.commands.game.cards.TradeMonopolyCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.ProgressCards.ProgressCardType;
import catan.settlers.server.model.game.handlers.set.BishopSetHandler;
import catan.settlers.server.model.game.handlers.set.CommercialHarborSetHandler;
import catan.settlers.server.model.game.handlers.set.MasterMerchantSetHandler;
import catan.settlers.server.model.game.handlers.set.MerchantFleetSetHandler;
import catan.settlers.server.model.game.handlers.set.MerchantSetHandler;
import catan.settlers.server.model.game.handlers.set.ResourceMonopolySetHandler;
import catan.settlers.server.model.game.handlers.set.TradeMonopolySetHandler;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Hexagon.IntersectionLoc;
import catan.settlers.server.model.map.Hexagon.TerrainType;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Village;

public class ProgressCardHandler implements Serializable {

	private static final long serialVersionUID = 4870364720277618607L;
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
			masterMerchant(sender);
			break;
		case MERCHANT:
			merchant(sender);
			break;
		case MERCHANT_FLEET:
			merchantFleet(sender);
			break;
		case RESOURCE_MONOPOLY:
			resourceMonopoly(sender);
			break;
		case TRADE_MONOPOLY:
			tradeMonopoly(sender);
			break;
		case BISHOP:
			bishop(sender);
			break;
		case CONSTITUTION:
			constitution(sender);
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
	 */
	private void merchant(Player sender) {

		MerchantSetHandler set = new MerchantSetHandler(game, sender);
		set.waitForPlayer(sender);

		game.setCurSetOfOpponentMove(set);

		// Send initial command
		String currentPlayerUsername = sender.getUsername();

		MerchantCommand cmd = new MerchantCommand(currentPlayerUsername);
		sender.sendCommand(cmd);

		// Update cards
		sender.useProgressCard(ProgressCardType.MERCHANT);
		sender.sendCommand(new UpdateCardsCommand(sender.getProgressCards()));

	}

	/**
	 * select a resource; you may trade that at 2:1 for this turn
	 */
	private void merchantFleet(Player sender) {
		MerchantFleetSetHandler set = new MerchantFleetSetHandler();
		set.waitForPlayer(sender);
		game.setCurSetOfOpponentMove(set);

		game.sendToAllPlayers(new MerchantFleetCommand(sender.getUsername()));

		// Update cards
		sender.useProgressCard(ProgressCardType.COMMERCIAL_HARBOR);
		sender.sendCommand(new UpdateCardsCommand(sender.getProgressCards()));
	}

	/**
	 * name a resource; all other players must give you 2 of that if they have
	 */
	private void resourceMonopoly(Player sender) {
		ResourceMonopolySetHandler set = new ResourceMonopolySetHandler();
		set.waitForPlayer(sender);
		game.setCurSetOfOpponentMove(set);
		game.sendToAllPlayers(new ResourceMonopolyCommand(sender.getUsername()));

		// Update cards
		sender.useProgressCard(ProgressCardType.TRADE_MONOPOLY);
		sender.sendCommand(new UpdateCardsCommand(sender.getProgressCards()));
	}

	/**
	 * name a commodity; all players must give you 1 if that if they have it
	 */
	private void tradeMonopoly(Player sender) {
		TradeMonopolySetHandler set = new TradeMonopolySetHandler();
		set.waitForPlayer(sender);
		game.setCurSetOfOpponentMove(set);
		game.sendToAllPlayers(new TradeMonopolyCommand(sender.getUsername()));

		// Update cards
		sender.useProgressCard(ProgressCardType.TRADE_MONOPOLY);
		sender.sendCommand(new UpdateCardsCommand(sender.getProgressCards()));
	}

	/**
	 * move the robber, but get a random card from EACH player on the robber's
	 * new hex
	 */
	private void bishop(Player sender) {
		BishopSetHandler set = new BishopSetHandler();
		set.waitForPlayer(sender);
		game.setCurSetOfOpponentMove(set);

		sender.sendCommand(new MoveRobberCommand(true));
		game.sendToAllPlayers(new BishopCommand(sender.getUsername()));

		// Update cards
		sender.useProgressCard(ProgressCardType.BISHOP);
		sender.sendCommand(new UpdateCardsCommand(sender.getProgressCards()));
	}

	/**
	 * one VP for player
	 */
	private void constitution(Player sender) {
		sender.incrementVP(1);
		game.getVictoryPoints().put(sender.getUsername(), sender.getVP()+1);
		sender.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
		// TODO: Update the victory points
	}

	/**
	 * choose an opponent who must remove a knight; place a knight of equal
	 * strength
	 */
	private void deserter(Player sender) {
		sender.sendCommand(new SelectPlayerCommand(SelectionReason.DESERTER));
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
							sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
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
		game.getVictoryPoints().put(sender.getUsername(), sender.getVP()+1);
		sender.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
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
