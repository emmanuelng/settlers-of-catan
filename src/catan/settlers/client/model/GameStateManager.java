package catan.settlers.client.model;

import java.util.ArrayList;
import java.util.HashMap;

import catan.settlers.network.server.commands.ClientToServerCommand;
import catan.settlers.network.server.commands.game.GetGameBoardCommand;
import catan.settlers.network.server.commands.game.GetListOfPlayersCommand;
import catan.settlers.server.model.Game.GamePhase;
import catan.settlers.server.model.Player;
import catan.settlers.server.model.Player.ResourceType;
import catan.settlers.server.model.ProgressCards.ProgressCardType;
import catan.settlers.server.model.map.Edge;
import catan.settlers.server.model.map.GameBoard;
import catan.settlers.server.model.map.Hexagon;
import catan.settlers.server.model.map.Intersection;
import catan.settlers.server.model.units.Port.PortKind;

public class GameStateManager {

	private int gameId;

	private GameBoard board;
	private ArrayList<String> participants;
	private String currentPlayer;
	private HashMap<ResourceType, Integer> resources;
	private HashMap<ProgressCardType, Integer> progressCards;
	private HashMap<ResourceType, Integer> receiveTradeOfferGive, receiveTradeOfferGet;
	private Player requestedPlayer;
	
	private Intersection selectedIntersection;
	private Edge selectedEdge;
	private Hexagon selectedHex;

	private boolean canMoveRobber;
	private boolean updateResources;
	private boolean updateProgressCards;
	private boolean updateBoard;
	private boolean updatePlayers;
	private boolean updateActions;
	private boolean showTradeMenu;
	private boolean showReceiveTradeMenu;
	private boolean showSevenDiscardMenu;

	private String dboxTitle;
	private String dBoxMessage;
	private String sevenDiscardMenuMsg;
	private String tradeMenuMsg;
	private GamePhase currentPhase;

	private HashMap<PortKind, Boolean> ownedPorts;




	public GameStateManager(int gameId) {
		this.gameId = gameId;
		this.canMoveRobber = false;

		this.updateResources = true;
		this.updateProgressCards = true;
		this.updateBoard = true;
		this.updatePlayers = true;
		this.updateActions = true;
		this.showTradeMenu = false;
		this.showSevenDiscardMenu = false;

		this.sevenDiscardMenuMsg = "";
		this.tradeMenuMsg = "";
	}

	public int getGameId() {
		return gameId;
	}

	public Intersection getSelectedIntersection() {
		return selectedIntersection;
	}

	public void setSelectedIntersection(Intersection selectedIntersection) {
		this.selectedIntersection = selectedIntersection;

		if (currentPhase == GamePhase.TURNPHASE) {
			this.selectedEdge = null;
			this.selectedHex = null;
		}

		this.updateBoard = true;
		this.updateActions = true;
	}

	public Edge getSelectedEdge() {
		return selectedEdge;
	}

	public void setSelectedEdge(Edge selectedEdge) {
		this.selectedEdge = selectedEdge;

		if (currentPhase == GamePhase.TURNPHASE) {
			this.selectedIntersection = null;
			this.selectedHex = null;
		}

		this.updateBoard = true;
		this.updateActions = true;
	}

	public GameBoard getBoard() {
		return board;
	}

	public void setSelectedHex(Hexagon hexagon) {
		if (currentPhase == GamePhase.TURNPHASE) {
			this.selectedHex = hexagon;
			this.selectedEdge = null;
			this.selectedIntersection = null;

			this.updateBoard = true;
			this.updateActions = true;
		}
	}

	public Hexagon getSelectedHex() {
		return selectedHex;
	}

	public void setBoard(GameBoard board) {
		this.board = board;
		this.updateBoard = true;
	}

	public ArrayList<String> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<String> participants) {
		this.participants = participants;
	}

	public void setCurrentPlayer(String player) {
		currentPlayer = player;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public HashMap<ResourceType, Integer> getResources() {
		if (resources == null) {
			resources = new HashMap<>();
			for (ResourceType rtype : ResourceType.values())
				resources.put(rtype, 0);
		}
		return resources;
	}

	public void setResources(HashMap<ResourceType, Integer> resources) {
		this.resources = resources;
		this.updateResources = true;
	}

	public HashMap<ProgressCardType, Integer> getProgressCards() {
		if (progressCards == null) {
			progressCards = new HashMap<>();
			for (ProgressCardType pCardType : ProgressCardType.values())
				progressCards.put(pCardType, 0);
		}
		return progressCards;
	}

	public void setProgressCards(HashMap<ProgressCardType, Integer> progressCards) {
		this.progressCards = progressCards;
		this.updateProgressCards = true;
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

	public boolean doUpdateResources() {
		boolean update = updateResources;
		updateResources = false;
		return update;
	}

	public boolean doUpdateProgressCards() {
		boolean update = updateProgressCards;
		updateProgressCards = false;
		return update;
	}

	public boolean doUpdateBoard() {
		boolean update = updateBoard;
		updateBoard = false;
		return update;
	}

	public boolean doUpdatePlayers() {
		boolean update = updatePlayers;
		updatePlayers = false;
		return update;
	}

	public boolean doUpdateActions() {
		boolean update = updateActions;
		updateActions = false;
		return update;
	}

	public String getdBoxTitle() {
		return dboxTitle;
	}

	public String getdBoxMessage() {
		return dBoxMessage;
	}

	public void setdBox(String title, String message) {
		dboxTitle = title;
		dBoxMessage = message;
	}

	public boolean doShowTradeMenu() {
		return showTradeMenu;
	}

	public void setShowTradeMenu(boolean b) {
		showTradeMenu = b;
	}

	public boolean doShowTradeReceivedMenu() {
		return showReceiveTradeMenu;
	}
	
	public void setShowTradeReceivedMenu(boolean b){
		showReceiveTradeMenu = b;
	}
	
	public void setReceivedTradeOffer(HashMap<ResourceType, Integer> whatYouGive, HashMap<ResourceType, Integer> whatYouGet, Player requestedPlayer){
		this.receiveTradeOfferGet = whatYouGive;
		this.receiveTradeOfferGive = whatYouGet;
		this.requestedPlayer = requestedPlayer;
	}
	
	public HashMap<ResourceType, Integer> tradeOfferReceivedWhatYouGet(){
		return receiveTradeOfferGive;
	}
	
	public HashMap<ResourceType, Integer> tradeOfferReceivedWhatYouGive(){
		return receiveTradeOfferGet;
	}
	
	public String getSevenDiscardMenuMsg() {
		return sevenDiscardMenuMsg;
	}

	public void setSevenDiscardMenuMsg(String msg) {
		sevenDiscardMenuMsg = msg;
	}

	public boolean doShowSevenDiscardMenu() {
		return showSevenDiscardMenu;
	}

	public void setShowSevenDiscardMenu(boolean b) {
		showSevenDiscardMenu = b;
	}

	public void setCurrentPhase(GamePhase currentPhase) {
		this.currentPhase = currentPhase;
	}

	public GamePhase getCurrentPhase() {
		return currentPhase;
	}

	public void setOwnedPorts(HashMap<PortKind, Boolean> ownedPorts) {
		this.ownedPorts = ownedPorts;
	}

	public HashMap<PortKind, Boolean> getOwnedPorts() {
		HashMap<PortKind, Boolean> ret = new HashMap<>();
		for (PortKind pkind : ownedPorts.keySet()) {
			ret.put(pkind, ownedPorts.get(pkind));
		}
		return ret;
	}

	public void setTradeMenuMessage(String msg) {
		this.tradeMenuMsg = msg;
	}

	public String getTradeMenuMsg() {
		return tradeMenuMsg;
	}
}
