package catan.settlers.server.model.game.handlers;

import java.io.Serializable;
import java.util.ArrayList;

import catan.settlers.network.client.commands.game.OwnedPortsChangedCommand;
import catan.settlers.network.client.commands.game.PlaceElmtsSetupPhaseCommand;
import catan.settlers.network.client.commands.game.RollDicePhaseCommand;
import catan.settlers.network.client.commands.game.TurnResponseCommand;
import catan.settlers.network.client.commands.game.UpdateResourcesCommand;
import catan.settlers.network.client.commands.game.UpdateVPCommand;
import catan.settlers.network.client.commands.game.WaitForPlayerCommand;
import catan.settlers.server.model.GameBoardManager;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.TurnData;
import catan.settlers.server.model.Game;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Port;
import catan.settlers.server.model.units.Village;

public class SetupPhaseHandler implements Serializable {

	private static final long serialVersionUID = 5592926389250804377L;

	private Game game;
	private Player currentPlayer;
	private GameBoardManager gameBoardManager;
	private ArrayList<Player> participants;
	private Intersection selectedIntersection;
	private Edge selectedEdge;

	public SetupPhaseHandler(Game game) {
		this.game = game;
	}

	/**
	 * Setup phase logic. There are two setup phases, where the player places
	 * his/her first roads and villages (a settlement in the first setup phase,
	 * a city in the second). The first phase goes in the regular order of
	 * players, the second goes in the reverse order.
	 */
	public void handle(Player sender, TurnData data, boolean isPhaseOne) {
		updateDataFromGame();

		if (sender == currentPlayer) {
			if (!isSelectionValid(data)) {
				return;
			} else {
				getIntersectionAndEdgeInstance(data);
				if (canBuildOnEdgeAndIntersection(sender)) {
					buildRoadAndVillage(isPhaseOne);
					if (!isPhaseOne)
						giveResourcesToPlayer();

					if (!isCurrentPhaseOver(isPhaseOne)) {
						goToNextPlayer(isPhaseOne);
					} else {
						setupNextPhase(isPhaseOne);
					}
				} else {
					sendNotBuildableErrorMsg();
				}
			}
		} else {
			// Ask to the player to wait
			sender.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
		}
	}

	/**
	 * Gets the required data from the game object such as the current player or
	 * the participants list
	 */
	private void updateDataFromGame() {
		this.currentPlayer = game.getCurrentPlayer();
		this.gameBoardManager = game.getGameBoardManager();
		this.participants = game.getParticipants();
	}

	/**
	 * Checks if the provided data contains a non-null intersection and edge
	 */
	private boolean isSelectionValid(TurnData data) {
		if (data.getEdgeSelection() == null || data.getIntersectionSelection() == null) {
			String message = "Please select an edge and an intersection";
			currentPlayer.sendCommand(new TurnResponseCommand(message, false));
			return false;
		}
		return true;
	}

	/**
	 * Gets the edge and intersection instances (this is useful since the edge
	 * and intersection instances from the turn data object are different from
	 * the ones stored in the game board object)
	 */
	private void getIntersectionAndEdgeInstance(TurnData data) {
		GameBoard board = gameBoardManager.getBoard();
		int intersectionId = data.getIntersectionSelection().getId();

		this.selectedIntersection = board.getIntersectionById(intersectionId);
		this.selectedEdge = board.getEdgeById(data.getEdgeSelection().getId());
	}

	/**
	 * Checks if it is possible to build on the provided edge and intersection
	 */
	private boolean canBuildOnEdgeAndIntersection(Player sender) {
		boolean areAdjacent = selectedEdge.hasIntersection(selectedIntersection);
		boolean isEdgeFree = selectedEdge.getOwner() == null;
		boolean isIntersectionBuildable = selectedIntersection.canBuild(sender, game.getGamePhase());

		return areAdjacent && isEdgeFree && isIntersectionBuildable;
	}

	/**
	 * Builds a road and a village on the provided places. In setup phase two,
	 * the village is automatically upgraded to a city. Sends an update command
	 * to all the players.
	 */
	private void buildRoadAndVillage(boolean isPhaseOne) {
		boolean isPortable = selectedIntersection.isPortable();
		Village village = new Village(currentPlayer, selectedIntersection);

		if (isPortable) {
			village = new Port(currentPlayer, selectedIntersection);
			currentPlayer.setPort(selectedIntersection.getPortKind());
			currentPlayer.sendCommand(new OwnedPortsChangedCommand(currentPlayer.getOwnedPorts()));
		}

		selectedIntersection.setUnit(village);
		selectedEdge.setOwner(currentPlayer);
		currentPlayer.incrementVP(1);
		

		if (!isPhaseOne){
			village.upgradeToCity();
			currentPlayer.incrementVP(1);
		}

		currentPlayer.sendCommand(new UpdateVPCommand(currentPlayer.getVP()));
		game.updateAllPlayers();
	}

	/**
	 * Produces the first resources of the player in setup phase two. Sends an
	 * update command to the current player.
	 */
	private void giveResourcesToPlayer() {
		for (Hexagon h : selectedIntersection.getHexagons()) {
			ResourceType r = Hexagon.terrainToResource(h.getType());
			if (r != null)
				currentPlayer.giveResource(r, 1);
		}

		UpdateResourcesCommand cmd = new UpdateResourcesCommand(currentPlayer.getResources());
		currentPlayer.sendCommand(cmd);
	}

	/**
	 * Checks if all the players have played in the current game phase.
	 */
	private boolean isCurrentPhaseOver(boolean isPhaseOne) {
		boolean isPhaseOneOver = currentPlayer == participants.get(participants.size() - 1);
		boolean isPhaseTwoOver = currentPlayer == participants.get(0);
		return isPhaseOne ? isPhaseOneOver : isPhaseTwoOver;
	}

	/**
	 * Sets the next current player, depending on the current game phase policy
	 * (clockwise in the setup phase one, counter clockwise in the second setup
	 * phase). Sends a command to display the instructions to the players
	 * (either play or wait).
	 */
	private void goToNextPlayer(boolean isPhaseOne) {
		Player nextPlayer = isPhaseOne ? game.nextPlayer() : game.previousPlayer();
		game.setCurrentPlayer(nextPlayer);

		for (Player p : participants) {
			if (p == nextPlayer) {
				p.sendCommand(new PlaceElmtsSetupPhaseCommand(isPhaseOne));
			} else {
				p.sendCommand(new WaitForPlayerCommand(nextPlayer.getUsername()));
			}
		}
	}

	/**
	 * Sets the next game phase. If the current phase is SETUPPHASEONE, go to
	 * SETUPPHASETWO. Otherwise, go to ROLLDICEPHASE.
	 */
	private void setupNextPhase(boolean isPhaseOne) {
		if (isPhaseOne) {
			// End of phase one
			game.setGamePhase(GamePhase.SETUPPHASETWO);
			for (Player p : participants) {
				if (p == currentPlayer) {
					p.sendCommand(new PlaceElmtsSetupPhaseCommand(false));
				} else {
					p.sendCommand(new WaitForPlayerCommand(currentPlayer.getUsername()));
				}
			}
		} else {
			// End of phase two
			game.setGamePhase(GamePhase.ROLLDICEPHASE);
			game.sendToAllPlayers(new RollDicePhaseCommand(currentPlayer.getUsername()));
			return;
		}
	}

	/**
	 * Sends an error message to the player if the edge or intersection
	 * selection is invalid.
	 */
	private void sendNotBuildableErrorMsg() {
		String message = "You cannot build here";
		if (!selectedEdge.hasIntersection(selectedIntersection)) {
			message = "The intersection and the edge must be adjacent";
		} else if (selectedEdge.getOwner() != null) {
			message = "You cannot build on an ocupied edge/intersection";
		}
		currentPlayer.sendCommand(new TurnResponseCommand(message, false));
	}
}
