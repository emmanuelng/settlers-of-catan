package catan.settlers.client.model;

import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.client.view.ClientWindow;
import catan.settlers.client.view.game.GameWindow;
import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.network.server.commands.game.GetGameBoardCommand;
import catan.settlers.network.server.commands.game.GetListOfPlayersCommand;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Intersection;

public class GameStateManager {

	private int gameId;

	private GameBoard board;
	private ArrayList<String> participants;
	private String currentPlayer;
	private HashMap<ResourceType, Integer> resources;

	private GameWindow gameWindow;

	private Intersection selectedIntersection;
	private Edge selectedEdge;
	private Hexagon selectedHex;

	private boolean canMoveRobber;

	public GameStateManager(int gameId) {
		this.gameId = gameId;
		this.gameWindow = ClientWindow.getInstance().getGameWindow();
		this.canMoveRobber = true;

		sync();
	}

	public int getGameId() {
		return gameId;
	}

	public Intersection getSelectedIntersection() {
		return selectedIntersection;
	}

	public void setSelectedIntersection(Intersection selectedIntersection) {
		this.selectedIntersection = selectedIntersection;
		if (gameWindow != null) {
			gameWindow.notifyBoardHasChanged();
			gameWindow.notifyUpdateActions();
		}
	}

	public Edge getSelectedEdge() {
		return selectedEdge;
	}

	public void setSelectedEdge(Edge selectedEdge) {
		this.selectedEdge = selectedEdge;
		if (gameWindow != null) {
			gameWindow.notifyBoardHasChanged();
			gameWindow.notifyUpdateActions();
		}
	}

	public GameBoard getBoard() {
		return board;
	}

	public void setSelectedHex(Hexagon hexagon) {
		this.selectedHex = hexagon;
		if (gameWindow != null) {
			gameWindow.notifyBoardHasChanged();
			gameWindow.notifyUpdateActions();
		}
	}

	public Hexagon getSelectedHex() {
		return selectedHex;
	}

	public void setBoard(GameBoard board) {
		this.board = board;
		if (gameWindow != null)
			gameWindow.notifyBoardHasChanged();
	}

	public ArrayList<String> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<String> participants) {
		this.participants = participants;
		if (gameWindow != null)
			gameWindow.notifyCurPlayerHasChanged();
	}

	public void setCurrentPlayer(String player) {
		currentPlayer = player;
		if (gameWindow != null)
			gameWindow.notifyCurPlayerHasChanged();
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public HashMap<ResourceType, Integer> getResources() {
		return resources;
	}

	public void setResources(HashMap<ResourceType, Integer> resources) {
		this.resources = resources;
		if (gameWindow != null)
			gameWindow.notifyResourcesHaveChanged();
	}

	public void sync() {
		ArrayList<ClientToServerCommand> cmds = new ArrayList<>();

		cmds.add(new GetListOfPlayersCommand());
		cmds.add(new GetGameBoardCommand());

		for (ClientToServerCommand cmd : cmds) {
			ClientModel.instance.getNetworkManager().sendCommand(cmd);
		}
	}

	public void setCanMoveRobber(boolean b) {
		this.canMoveRobber = b;
	}

	public boolean canMoveRobber() {
		return canMoveRobber;
	}

}
