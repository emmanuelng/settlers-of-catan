package catan.settlers.server.model.game.handlers;

import catan.settlers.network.client.commands.game.UpdateGameBoardCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.GameBoardManager;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Cost;
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
import catan.settlers.server.model.units.Knight.KnightType;
import catan.settlers.server.model.units.Port;
import catan.settlers.server.model.units.Village;

public class TurnPhaseHandler {

	private Game game;
	private GameBoardManager gameBoardManager;
	private Player currentPlayer;
	private Edge selectedEdge;
	private Intersection selectedIntersection;

	public TurnPhaseHandler(Game game) {
		this.game = game;
	}

	public void handle(Player sender, TurnData data) {
		updateDataFromGame();

		if (!currentPlayer.getUsername().equals(sender.getUsername()))
			return;

		getSelectedEdgeAndIntersectionInstances(data);

		switch (data.getAction()) {
		case BUILDSETTLEMENT:
			buildSettlement(sender);
			break;
		case BUILDROAD:
			buildRoad(sender);
			break;
		case UPGRADESETTLEMENT:
			upgradeSettlement(sender);
			break;
		case BUILDKNIGHT:
			buildKnight(sender);
			break;
		case UPGRADEKNIGHT:
			updateKnight(sender);
			break;
		case ENDTURN:
			endTurn();
			break;
		}
	}

	private void updateDataFromGame() {
		this.gameBoardManager = game.getGameBoardManager();
		this.currentPlayer = game.getCurrentPlayer();
	}

	private void getSelectedEdgeAndIntersectionInstances(TurnData data) {
		GameBoard board = gameBoardManager.getBoard();

		int selectedgeId = data.getEdgeSelection().getId();
		this.selectedEdge = board.getEdgeById(selectedgeId);

		int selectedIntersectionId = data.getIntersectionSelection().getId();
		this.selectedIntersection = board.getIntersectionById(selectedIntersectionId);
	}

	private void buildSettlement(Player sender) {
		if (selectedIntersection.canBuild()) {
			boolean isPortable = selectedIntersection.isPortable();
			Village village = isPortable ? new Port(sender) : new Village(sender);
			Cost cost = village.getBuildSettlementCost();

			if (cost.canPay(sender)) {
				selectedIntersection.setUnit(village);
				updateResourcesAndBoard(sender);
			}
		}
	}

	private void buildRoad(Player sender) {
		Cost cost = selectedEdge.getBuildRoadCost();

		if (cost.canPay(sender)) {
			selectedEdge.setOwner(sender);
			cost.removeResources(sender);
			updateResourcesAndBoard(sender);
		}
	}

	private void upgradeSettlement(Player sender) {
		IntersectionUnit unit = selectedIntersection.getUnit();
		if (unit instanceof Village) {
			Village village = (Village) unit;
			Cost cost = village.getUpgradeToCityCost();
			if (cost.canPay(sender)) {
				village.upgradeToCity();
				cost.removeResources(sender);
			}
		}
	}

	private void buildKnight(Player sender) {
		if (selectedIntersection.getUnit() == null) {
			Knight knight = new Knight(sender);
			Cost cost = knight.getBuildKnightCost();
			if (cost.canPay(sender) && selectedIntersection.connected(sender)) {
				selectedIntersection.setUnit(knight);
				cost.removeResources(sender);
			}
		}
	}

	private void updateKnight(Player sender) {
		IntersectionUnit unit = selectedIntersection.getUnit();
		if (unit instanceof Knight) {
			Knight knight = (Knight) unit;
			Cost cost = knight.getUpdateKnightCost();

			switch (knight.getKnightType()) {
			case BASIC_KNIGHT:
				if (sender.canHire(KnightType.STRONG_KNIGHT))
					break;
				return;
			case STRONG_KNIGHT:
				if (sender.canHire(KnightType.MIGHTY_KNIGHT) && sender.hasBarracks())
					break;
				return;
			case MIGHTY_KNIGHT:
				return;
			}

			// Upgrade knight
			if (cost.canPay(sender)) {
				knight.upgradeKnight();
				cost.removeResources(sender);
				updateResourcesAndBoard(sender);
			}
		}
	}

	private void endTurn() {
		Player nextPlayer = game.nextPlayer();
		game.setCurrentPlayer(nextPlayer);

		// TODO send command to start next turn
	}

	private void updateResourcesAndBoard(Player sender) {
		sender.sendCommand(new UpdateResourcesCommand(sender.getResources()));
		sender.sendCommand(new UpdateGameBoardCommand(gameBoardManager.getBoardDeepCopy()));
	}

}
