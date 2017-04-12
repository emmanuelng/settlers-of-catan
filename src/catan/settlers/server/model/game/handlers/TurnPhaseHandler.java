package catan.settlers.server.model.game.handlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import catan.settlers.client.model.GameStateManager.SelectionReason;
import catan.settlers.client.view.game.FlipchartLayer.Field;
import catan.settlers.network.client.commands.game.FailureCommand;
import catan.settlers.network.client.commands.game.MoveDisplacedKnightCommand;
import catan.settlers.network.client.commands.game.MoveRobberCommand;
import catan.settlers.network.client.commands.game.OwnedPortsChangedCommand;
import catan.settlers.network.client.commands.game.RollDicePhaseCommand;
import catan.settlers.network.client.commands.game.UpdateCardsCommand;
import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateLargestArmyCommand;
import catan.settlers.network.client.commands.game.UpdatePlayerLevelsCommand;
import catan.settlers.network.client.commands.game.UpdatePoliticsMetOwnerCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.UpdateScienceMetOwnerCommand;
import catan.settlers.network.client.commands.game.UpdateTradeMetOwnerCommand;
import catan.settlers.network.client.commands.game.UpdateVPCommand;
import catan.settlers.network.server.Credentials;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.GameBoardManager;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.game.handlers.set.DisplacedKnightHandler;
import catan.settlers.server.model.game.handlers.set.SetOfOpponentMove;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Cost;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Knight.KnightType;
import catan.settlers.server.model.units.Port;
import catan.settlers.server.model.units.Village;
import catan.settlers.server.model.units.Village.VillageKind;

public class TurnPhaseHandler implements Serializable {

	private static final long serialVersionUID = -2516426252767477921L;

	private Game game;
	private GameBoardManager gameBoardManager;
	private Player currentPlayer;
	private Edge selectedEdge;
	private Intersection selectedIntersection;
	private Knight selectedKnight;
	private HashMap<Field, Player> metOwners;
	private FishHandler fishHandler;
	private HashSet<Edge> visitedEdges;

	public TurnPhaseHandler(Game game) {
		this.game = game;
		metOwners = new HashMap<Field, Player>();
		for (Field f : Field.values()) {
			metOwners.put(f, null);
		}
		fishHandler = new FishHandler(game);
	}

	public void handle(Player sender, TurnData data) {
		updateDataFromGame();

		if (!currentPlayer.getUsername().equals(sender.getUsername()))
			return;

		getSelectedEdgeAndIntersectionInstances(data);

		switch (data.getAction()) {
		case BUILD_SETTLEMENT:
			buildSettlement();
			break;
		case BUILD_ROAD:
			buildRoad();
			break;
		case UPGRADE_SETTLEMENT:
			upgradeSettlement();
			break;
		case BUILD_KNIGHT:
			buildKnight();
			break;
		case UPGRADE_KNIGHT:
			promoteKnight();
			break;
		case ACTIVATE_KNIGHT:
			activateKnight();
			break;
		case BUILD_WALL:
			buildWall();
			break;
		case BUILD_SHIP:
			buildShip();
			break;
		case PROGRESS_CARD:
			game.getProgressCardHandler().handle(sender, data.getProgressCard());
			break;
		case END_TURN:
			endTurn();
			break;
		case DISPLACE_KNIGHT:
			displaceKnight();
			break;
		case RESOURCE_SELECTED:
			currentPlayer.giveResource(data.getSelectedResourceOrCommodity(), 1);
			currentPlayer.sendCommand(new UpdateResourcesCommand(currentPlayer.getResources()));
			break;
		case PLAYER_SELECTED:
			handlePlayerSelected(data);
			break;
		case FISH_ACTION:
			fishHandler.handle(sender, data.getFishAction());
			break;
		case POLITICS_CITY_IMPROVEMENT:
			PoliticsCityImprovement();
			break;
		case SCIENCE_CITY_IMPROVEMENT:
			ScienceCityImprovement();
			break;
		case TRADE_CITY_IMPROVEMENT:
			TradeCityImprovement();
			break;
		case DRAW_PROGRESS_CARD:
			drawProgressCard(data);
			break;
		default:
			break;
		}
	}

	private void drawProgressCard(TurnData data) {
		if (data.getSelectedProgressCardType().equals("TRADE")) {
			currentPlayer.giveProgressCard(game.getProgressCards().drawTradeCard());
		} else if (data.getSelectedProgressCardType().equals("POLITICS")) {
			currentPlayer.giveProgressCard(game.getProgressCards().drawPoliticsCard());
		} else if (data.getSelectedProgressCardType().equals("SCIENCE")) {
			currentPlayer.giveProgressCard(game.getProgressCards().drawScienceCard());
		}
		currentPlayer.sendCommand(new UpdateCardsCommand(currentPlayer.getProgressCards()));
	}

	private void handlePlayerSelected(TurnData data) {
		SelectionReason reason = data.getSelectionReason();
		switch (reason) {
		case STEAL_RESOURCE:
			stealResource(data.getSelectedPlayer());
			break;
		case DESERTER:

			break;
		default:
			break;
		}
	}

	private void stealResource(String username) {
		Player target = game.getPlayersManager().getPlayerByUsername(username);
		ResourceType r = target.drawRandomResource();
		target.removeResource(r, 1);
		currentPlayer.giveResource(r, 1);
		for (Player p : game.getParticipants()) {
			p.sendCommand(new UpdateResourcesCommand(p.getResources()));
		}
	}

	private void updateDataFromGame() {
		this.gameBoardManager = game.getGameBoardManager();
		this.currentPlayer = game.getCurrentPlayer();
	}

	private void getSelectedEdgeAndIntersectionInstances(TurnData data) {
		GameBoard board = gameBoardManager.getBoard();

		if (data.getEdgeSelection() != null) {
			int selectedgeId = data.getEdgeSelection().getId();
			this.selectedEdge = board.getEdgeById(selectedgeId);
		}

		if (data.getIntersectionSelection() != null) {
			int selectedIntersectionId = data.getIntersectionSelection().getId();
			this.selectedIntersection = board.getIntersectionById(selectedIntersectionId);
		}

		if (data.getSelectedHex(game.getGameBoardManager().getBoard()) != null) {
			data.getSelectedHex(game.getGameBoardManager().getBoard());
		}

		this.selectedKnight = data.getSelectedKnight();
	}

	private void buildSettlement() {
		if (selectedIntersection.canBuild(currentPlayer, game.getGamePhase())) {
			boolean isPortable = selectedIntersection.isPortable();
			Village village = new Village(currentPlayer, selectedIntersection);

			if (isPortable) {
				village = new Port(currentPlayer, selectedIntersection);
				currentPlayer.setPort(selectedIntersection.getPortKind());
				currentPlayer.sendCommand(new OwnedPortsChangedCommand(currentPlayer.getOwnedPorts()));
			}

			Cost cost = village.getBuildSettlementCost();

			if (cost.canPay(currentPlayer)) {
				selectedIntersection.setUnit(village);
				currentPlayer.incrementVP(1);
				currentPlayer.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
				updateResourcesAndBoard();
			}
		}
	}

	private void buildRoad() {
		Cost cost;
		if (currentPlayer.hasRoadBuilding()) {
			cost = new Cost();
			currentPlayer.useRoadBuilding();
		} else {
			cost = selectedEdge.getBuildRoadCost();
		}
		if (cost.canPay(currentPlayer)) {
			selectedEdge.setOwner(currentPlayer);
			cost.removeResources(currentPlayer);
			updateResourcesAndBoard();
			// updateLongestRoad(selectedEdge, currentPlayer);
		}

	}

	private void upgradeSettlement() {
		IntersectionUnit unit = selectedIntersection.getUnit();
		if (unit instanceof Village) {
			Village village = (Village) unit;
			Cost cost;
			if (currentPlayer.hasMedicine()) {
				cost = new Cost();
				cost.addPriceEntry(ResourceType.ORE, 2);
				cost.addPriceEntry(ResourceType.GRAIN, 1);
				currentPlayer.useMedicine();
			} else {
				cost = village.getUpgradeToCityCost();
			}
			if (cost.canPay(currentPlayer)) {
				village.upgradeToCity();
				cost.removeResources(currentPlayer);
				currentPlayer.incrementVP(1);
				currentPlayer.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
				updateResourcesAndBoard();
			}
		}
	}

	private void buildWall() {
		IntersectionUnit unit = selectedIntersection.getUnit();
		if (unit instanceof Village) {
			Village village = (Village) unit;
			Cost cost = new Cost();
			// if some progress card changes cost here, dont know right now
			if (!currentPlayer.hasEngineer()) {
				cost = village.getbuildWallCost();
			}

			if (cost.canPay(currentPlayer)) {
				if (village.getKind() == VillageKind.SETTLEMENT) {
					currentPlayer.sendCommand(new FailureCommand("Cannot build walls on a settlement"));
				} else if (currentPlayer.getNumberOfWalls() >= 3) {
					currentPlayer.sendCommand(new FailureCommand("You cannot build walls anymore"));
				} else {
					village.buildWall();
					cost.removeResources(currentPlayer);
					updateResourcesAndBoard();
				}
			}
		}
	}

	private void buildShip() {
		Cost cost;
		if (currentPlayer.hasRoadBuilding()) {
			cost = new Cost();
			currentPlayer.useRoadBuilding();
		} else {
			cost = selectedEdge.getBuildShipCost();
		}
		if (cost.canPay(currentPlayer)) {
			selectedEdge.setOwner(currentPlayer);
			cost.removeResources(currentPlayer);
			updateResourcesAndBoard();
			// updateLongestRoad(selectedEdge, currentPlayer);
		}
	}

	private void buildKnight() {
		if (selectedIntersection.getUnit() == null && !selectedIntersection.isMaritime()) {
			Knight knight = new Knight(currentPlayer, selectedIntersection);
			Cost cost = knight.getBuildKnightCost();
			if (cost.canPay(currentPlayer) && selectedIntersection.connected(currentPlayer)) {
				selectedIntersection.setUnit(knight);
				cost.removeResources(currentPlayer);
				updateResourcesAndBoard();
			}
		}
		updateLargestArmy();
	}

	private void promoteKnight() {
		IntersectionUnit unit = selectedIntersection.getUnit();
		if (unit instanceof Knight) {
			Knight knight = (Knight) unit;
			Cost cost;
			if (currentPlayer.hasSmith()) {
				cost = new Cost();
				currentPlayer.useSmith();
			} else {
				cost = knight.getUpdateKnightCost();
			}

			switch (knight.getType()) {
			case BASIC_KNIGHT:
				if (currentPlayer.canHire(KnightType.STRONG_KNIGHT)) {
					if (cost.canPay(currentPlayer)) {
						knight.upgradeKnight();
						cost.removeResources(currentPlayer);
						updateResourcesAndBoard();
					}
				}
				return;
			case STRONG_KNIGHT:
				if (currentPlayer.canHire(KnightType.MIGHTY_KNIGHT) && currentPlayer.hasBarracks()) {
					if (cost.canPay(currentPlayer)) {
						knight.upgradeKnight();
						cost.removeResources(currentPlayer);
						updateResourcesAndBoard();
					}
				}
				return;
			case MIGHTY_KNIGHT:
				return;
			}
		}
		updateLargestArmy();
	}

	private void activateKnight() {
		IntersectionUnit unit = selectedIntersection.getUnit();
		if (unit instanceof Knight) {
			Knight knight = (Knight) unit;
			Cost cost = knight.getActivateKnightCost();
			if (cost.canPay(currentPlayer)) {
				knight.activateKnight();
				cost.removeResources(currentPlayer);
				updateResourcesAndBoard();
			}
		}
	}

	private void displaceKnight() {
		GameBoard board = gameBoardManager.getBoard();
		Intersection newLocation = selectedIntersection;
		Intersection curKnightLoc = board.getIntersectionById(selectedKnight.getLocatedAt().getId());

		if (newLocation.isMaritime())
			return;

		if (!(curKnightLoc.getUnit() instanceof Knight))
			return;

		if (selectedKnight != null) {
			if (selectedKnight.canCanMoveIntersecIds().contains(newLocation.getId())) {
				if (newLocation.getUnit() instanceof Knight) {
					if (selectedKnight.getType().ordinal() > ((Knight) newLocation.getUnit()).getType().ordinal()) {
						Knight knight = (Knight) newLocation.getUnit();
						curKnightLoc.setUnit(null);
						updateResourcesAndBoard();

						Player displacee = newLocation.getUnit().getOwner();
						SetOfOpponentMove displacedKnight = new DisplacedKnightHandler(knight);
						System.out.println("Sending the move displaced knight command");
						displacee.sendCommand(new MoveDisplacedKnightCommand(newLocation));
						displacedKnight.waitForPlayer(displacee);
						game.setCurSetOfOpponentMove(displacedKnight);

						selectedKnight.setLocatedAt(newLocation);
						newLocation.setUnit(selectedKnight);

						selectedKnight.deactivateKnight();
						updateResourcesAndBoard();
					}
				} else if (newLocation.getUnit() instanceof Village) {
					return;
				} else {
					selectedKnight.setLocatedAt(newLocation);
					newLocation.setUnit(selectedKnight);
					curKnightLoc.setUnit(null);
					selectedKnight.deactivateKnight();
					updateResourcesAndBoard();
				}
				HashSet<Hexagon> hexes = newLocation.getHexagons();
				for (Hexagon h : hexes) {
					if (h == game.getGameBoardManager().getBoard().getRobberHex()) {
						currentPlayer.sendCommand(new MoveRobberCommand(false));
					}
				}
			}
		}
	}

	private void endTurn() {
		Player nextPlayer = game.nextPlayer();
		game.setCurrentPlayer(nextPlayer);

		// Reset merchant fleet advantages
		for (Player p : game.getParticipants())
			p.resetTradeAtAdvantage();

		if (game.getCurrentPlayer().getVP() >= 13) {
			if (!game.getCurrentPlayer().getHasBoot()) {
				game.declareVictor(currentPlayer);
			} else if (game.getCurrentPlayer().getHasBoot() && game.getCurrentPlayer().getVP() >= 14) {
				game.declareVictor(currentPlayer);
			}
		}

		game.setGamePhase(GamePhase.ROLLDICEPHASE);
		game.sendToAllPlayers(new RollDicePhaseCommand(nextPlayer.getUsername()));
	}

	private void updateResourcesAndBoard() {
		for (Player p : game.getParticipants()) {
			p.sendCommand(new UpdateResourcesCommand(currentPlayer.getResources()));
			p.sendCommand(new UpdateGameBoardCommand(gameBoardManager.getBoardDeepCopy()));
		}
	}

	private void updatePlayerCondition() {
		currentPlayer.sendCommand(new UpdatePlayerLevelsCommand(currentPlayer.getPoliticsLevel(),
				currentPlayer.getTradeLevel(), currentPlayer.getScienceLevel()));
	}

	private void PoliticsCityImprovement() {

		int level = currentPlayer.getPoliticsLevel() + 1;
		Cost cost = new Cost();
		if (currentPlayer.hasCrane()) {
			cost.addPriceEntry(ResourceType.COIN, level - 1);
		} else {
			cost.addPriceEntry(ResourceType.COIN, level);
		}

		if (cost.canPay(currentPlayer)) {
			currentPlayer.setPoliticsLvl(level);
			cost.removeResources(currentPlayer);
			updateResourcesAndBoard();
			updatePlayerCondition();

			updateMetOwners(Field.POLITICS);
		}
	}

	private void ScienceCityImprovement() {
		int level = currentPlayer.getScienceLevel() + 1;
		Cost cost = new Cost();
		if (currentPlayer.hasCrane()) {
			cost.addPriceEntry(ResourceType.PAPER, level - 1);
		} else {
			cost.addPriceEntry(ResourceType.PAPER, level);
		}

		if (cost.canPay(currentPlayer)) {
			currentPlayer.setScienceLvl(level);
			cost.removeResources(currentPlayer);
			updateResourcesAndBoard();
			updatePlayerCondition();

			updateMetOwners(Field.SCIENCE);
		}
	}

	private void TradeCityImprovement() {
		int level = currentPlayer.getTradeLevel() + 1;
		Cost cost = new Cost();
		if (currentPlayer.hasCrane()) {
			cost.addPriceEntry(ResourceType.CLOTH, level - 1);
		} else {
			cost.addPriceEntry(ResourceType.CLOTH, level);
		}
		if (cost.canPay(currentPlayer)) {
			currentPlayer.setTradeLvl(level);
			cost.removeResources(currentPlayer);
			updateResourcesAndBoard();
			updatePlayerCondition();
			updateMetOwners(Field.TRADE);
		}
	}

	public void updateMetOwners(Field f) {
		ArrayList<Player> otherPlayers = game.getParticipants();
		otherPlayers.remove(currentPlayer);
		int otherPeoplelvl = 0;

		switch (f) {
		case SCIENCE:
			for (Player p : otherPlayers) {
				if (p.getScienceLevel() > otherPeoplelvl) {
					otherPeoplelvl = p.getScienceLevel();
				}
			}
			System.out.println("This happens");
			if (currentPlayer.getScienceLevel() == 4) {
				metOwners.put(Field.SCIENCE, currentPlayer);
				currentPlayer.incrementVP(1);
				currentPlayer.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
			} else if (currentPlayer.getScienceLevel() == 5 && currentPlayer.getScienceLevel() > otherPeoplelvl) {
				metOwners.get(Field.SCIENCE).decrementVP(1);
				metOwners.get(Field.SCIENCE).sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
				metOwners.put(Field.SCIENCE, currentPlayer);
				currentPlayer.incrementVP(1);
				currentPlayer.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
			}
			if (metOwners.get(Field.SCIENCE) != null) {
				game.sendToAllPlayers(new UpdateScienceMetOwnerCommand(metOwners.get(Field.SCIENCE).getUsername()));
			}
			break;
		case TRADE:
			for (Player p : otherPlayers) {
				if (p.getTradeLevel() > otherPeoplelvl) {
					otherPeoplelvl = p.getTradeLevel();
				}
			}
			if (currentPlayer.getTradeLevel() == 4) {
				metOwners.put(Field.TRADE, currentPlayer);
				currentPlayer.incrementVP(1);
				currentPlayer.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
			} else if (currentPlayer.getTradeLevel() == 5 && currentPlayer.getTradeLevel() > otherPeoplelvl) {
				metOwners.get(Field.TRADE).decrementVP(1);
				metOwners.get(Field.TRADE).sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
				metOwners.put(Field.TRADE, currentPlayer);
				currentPlayer.incrementVP(1);
				currentPlayer.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
			}
			if (metOwners.get(Field.TRADE) != null) {
				game.sendToAllPlayers(new UpdateTradeMetOwnerCommand(metOwners.get(Field.TRADE).getUsername()));
			}
			break;
		case POLITICS:
			for (Player p : otherPlayers) {
				if (p.getPoliticsLevel() > otherPeoplelvl) {
					otherPeoplelvl = p.getPoliticsLevel();
				}
			}
			if (currentPlayer.getPoliticsLevel() == 4) {
				metOwners.put(Field.POLITICS, currentPlayer);
				currentPlayer.incrementVP(1);
				currentPlayer.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
			} else if (currentPlayer.getPoliticsLevel() == 5 && currentPlayer.getPoliticsLevel() > otherPeoplelvl) {
				metOwners.get(Field.POLITICS).decrementVP(1);
				metOwners.get(Field.POLITICS).sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
				metOwners.put(Field.POLITICS, currentPlayer);
				currentPlayer.incrementVP(1);
				currentPlayer.sendCommand(new UpdateVPCommand(game.getVictoryPoints()));
			}
			if (metOwners.get(Field.POLITICS) != null) {
				game.sendToAllPlayers(new UpdatePoliticsMetOwnerCommand(metOwners.get(Field.POLITICS).getUsername()));
			}
			break;
		}

	}

	public void updateLargestArmy() {
		HashMap<Player, Integer> playerStrength = new HashMap<>();
		for (Player p : game.getParticipants())
			playerStrength.put(p, 0);

		/*
		 * Checks all intersections. If city, +1 to barbarian strength. If
		 * active knight, +1/2/3 to player strength
		 */
		for (Intersection i : gameBoardManager.getBoard().getIntersections()) {
			IntersectionUnit unit = i.getUnit();
			if (unit instanceof Knight) {
				int current = 0;
				switch (((Knight) unit).getType()) {
				case BASIC_KNIGHT:
					current = playerStrength.get(unit.getOwner());
					playerStrength.put(unit.getOwner(), current + 1);
					break;
				case STRONG_KNIGHT:
					current = playerStrength.get(unit.getOwner());
					playerStrength.put(unit.getOwner(), current + 2);
					break;
				case MIGHTY_KNIGHT:
					current = playerStrength.get(unit.getOwner());
					playerStrength.put(unit.getOwner(), current + 3);
					break;
				}

			}
		}

		int maxStrength = 0;
		Player strongestPlayer = new Player(new Credentials(null, null));
		for (Player player : game.getParticipants()) {
			int curPlayerStrength = playerStrength.get(player);
			if (curPlayerStrength >= maxStrength && curPlayerStrength >= 5) {
				strongestPlayer = player;
				maxStrength = curPlayerStrength;
				game.setLargestArmy(strongestPlayer);
				game.sendToAllPlayers(new UpdateLargestArmyCommand(strongestPlayer.getUsername()));
			}
		}
	}
}
