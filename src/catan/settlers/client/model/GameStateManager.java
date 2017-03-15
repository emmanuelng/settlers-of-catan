package catan.settlers.client.model;

import java.util.ArrayList;

import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Intersection;

public class GameStateManager {

	private int gameId;
	private Intersection selectedIntersection;
	private Edge selectedEdge;
	private GameBoard board;
	private ArrayList<String> participants;

	public GameStateManager(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}

	public Intersection getSelectedIntersection() {
		return selectedIntersection;
	}

	public void setSelectedIntersection(Intersection selectedIntersection) {
		this.selectedIntersection = selectedIntersection;
	}

	public Edge getSelectedEdge() {
		return selectedEdge;
	}

	public void setSelectedEdge(Edge selectedEdge) {
		this.selectedEdge = selectedEdge;
	}

	public GameBoard getBoard() {
		return board;
	}

	public void setBoard(GameBoard board) {
		this.board = board;
	}

	public ArrayList<String> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<String> participants) {
		this.participants = participants;
	}

}
