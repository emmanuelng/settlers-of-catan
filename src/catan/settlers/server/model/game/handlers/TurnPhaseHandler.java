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
			buildSettlement();
			break;
		case BUILDROAD:
			buildRoad();
			break;
		case UPGRADESETTLEMENT:
			upgradeSettlement();
			break;
		case BUILDKNIGHT:
			buildKnight();
			break;
		case UPGRADEKNIGHT:
			updateKnight();
			break;
		case ACTIVATEKNIGHT:
			activateKnight();
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

		if (data.getEdgeSelection() != null) {
			int selectedgeId = data.getEdgeSelection().getId();
			this.selectedEdge = board.getEdgeById(selectedgeId);
		}

		if (data.getIntersectionSelection() != null) {
			int selectedIntersectionId = data.getIntersectionSelection().getId();
			this.selectedIntersection = board.getIntersectionById(selectedIntersectionId);
		}
	}

	private void buildSettlement() {
		if (selectedIntersection.canBuild(currentPlayer, game.getGamePhase())) {
			boolean isPortable = selectedIntersection.isPortable();
			Village village = isPortable ? new Port(currentPlayer) : new Village(currentPlayer);
			Cost cost = village.getBuildSettlementCost();

			if (cost.canPay(currentPlayer)) {
				selectedIntersection.setUnit(village);
				updateResourcesAndBoard();
			}
		}
	}

	private void buildRoad() {
		Cost cost = selectedEdge.getBuildRoadCost();

		if (cost.canPay(currentPlayer)) {
			selectedEdge.setOwner(currentPlayer);
			cost.removeResources(currentPlayer);
			updateResourcesAndBoard();
		}
	}

	private void upgradeSettlement() {
		IntersectionUnit unit = selectedIntersection.getUnit();
		if (unit instanceof Village) {
			Village village = (Village) unit;
			Cost cost = village.getUpgradeToCityCost();
			if (cost.canPay(currentPlayer)) {
				village.upgradeToCity();
				cost.removeResources(currentPlayer);
			}
		}
	}

	private void buildKnight() {
		if (selectedIntersection.getUnit() == null) {
			Knight knight = new Knight(currentPlayer);
			Cost cost = knight.getBuildKnightCost();
			if (cost.canPay(currentPlayer) && selectedIntersection.connected(currentPlayer)) {
				selectedIntersection.setUnit(knight);
				cost.removeResources(currentPlayer);
			}
		}
	}

	private void updateKnight() {
		IntersectionUnit unit = selectedIntersection.getUnit();
		if (unit instanceof Knight) {
			Knight knight = (Knight) unit;
			Cost cost = knight.getUpdateKnightCost();

			switch (knight.getKnightType()) {
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
	}
	
	private void activateKnight() {
		
	}

	private void endTurn() {
		Player nextPlayer = game.nextPlayer();
		game.setCurrentPlayer(nextPlayer);

		// TODO send command to start next turn
	}

	private void updateResourcesAndBoard() {
		currentPlayer.sendCommand(new UpdateResourcesCommand(currentPlayer.getResources()));
		currentPlayer.sendCommand(new UpdateGameBoardCommand(gameBoardManager.getBoardDeepCopy()));
	}

}
