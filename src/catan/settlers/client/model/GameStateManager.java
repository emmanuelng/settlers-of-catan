package catan.settlers.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
import catan.settlers.server.model.units.IntersectionUnit;
import catan.settlers.server.model.units.Knight;
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
	private Knight selectedKnight;

	private boolean attacked;
	private boolean canMoveRobber;
	private boolean isBishop;
	private boolean updateResources;
	private boolean updateProgressCards;
	private boolean updateBoard;
	private boolean updatePlayers;
	private boolean updateActions;
	private boolean showTradeMenu;
	private boolean showReceiveTradeMenu;
	private boolean showSevenDiscardMenu;
	private boolean showSelectResourceMenu;
	private boolean moveKnightMode;
	private boolean showSelectPlayerMenu;
	private boolean showProgressCardMenu;
	private boolean showSelectCardTypeMenu;
	private boolean showSelectCommodityMenu;
	private boolean doShowSelectHexLayer;

	private String dboxTitle;
	private String dBoxMessage;
	private String sevenDiscardMenuMsg;
	private String selectResourceMsg;
	private String selectCommodityMsg;
	private String tradeMenuMsg;
	private String showSelectResourceReason;
	private GamePhase currentPhase;
	private ArrayList<String> playersToShow;

	private HashMap<PortKind, Boolean> ownedPorts;

	// List of intersections where the selected knight can move
	private HashSet<Integer> canMoveKnightIntersecIds;

	private int tradeImprovementLevel;
	private int politicsImprovementLevel;
	private int scienceImprovementLevel;
	private int barbarianCounter;
	private ArrayList<ResourceType> merchantFleetAdvantage;

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
		this.showSelectPlayerMenu = false;
		this.showSelectResourceMenu = false;
		this.showSelectCardTypeMenu = false;
		this.showSelectCommodityMenu = false;
		this.doShowSelectHexLayer = false;

		this.sevenDiscardMenuMsg = "";
		this.tradeMenuMsg = "";
		this.selectResourceMsg = "";
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
		updateActions = true;
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
		HashMap<ProgressCardType, Integer> ret = new HashMap<>();
		ret.putAll(progressCards);
		return progressCards;
	}

	public void setProgressCards(HashMap<ProgressCardType, Integer> progressCards) {
		this.progressCards = progressCards;
		this.progressCards.put(ProgressCardType.BISHOP, 1);
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

	public void setCanMoveRobber(boolean b, boolean isBishop) {
		this.canMoveRobber = b;
		this.isBishop = isBishop;
	}

	public boolean canMoveRobber() {
		return canMoveRobber;
	}

	public boolean moveRobberIsBishop() {
		return isBishop;
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

	public void setShowTradeReceivedMenu(boolean b) {
		showReceiveTradeMenu = b;
	}

	public Player getProposedPlayer() {
		return requestedPlayer;
	}

	public void setReceivedTradeOffer(HashMap<ResourceType, Integer> whatYouGive,
			HashMap<ResourceType, Integer> whatYouGet, Player requestedPlayer) {
		this.receiveTradeOfferGet = whatYouGive;
		this.receiveTradeOfferGive = whatYouGet;
		this.requestedPlayer = requestedPlayer;
	}

	public HashMap<ResourceType, Integer> tradeOfferReceivedWhatYouGet() {
		return receiveTradeOfferGive;
	}

	public HashMap<ResourceType, Integer> tradeOfferReceivedWhatYouGive() {
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
		updateActions = true;
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

	public void setMoveKnightMode(boolean b) {
		Intersection selected = selectedIntersection;
		if (b && selected != null) {
			IntersectionUnit unit = selected.getUnit();
			if (unit != null)
				if (unit instanceof Knight) {
					this.moveKnightMode = true;
					this.selectedKnight = (Knight) unit;
					return;
				}
		}

		this.selectedKnight = null;
		this.moveKnightMode = false;
		this.updateActions = true;
		this.updateBoard = true;
	}

	public boolean isMoveKnightMode() {
		return moveKnightMode;
	}

	public void setCanMoveKnightIntersecIds(HashSet<Integer> canCanMoveIntersecIds) {
		this.canMoveKnightIntersecIds = canCanMoveIntersecIds;
		updateBoard = true;
		updateActions = true;

	}

	public HashSet<Integer> getCanMoveKnightIntersecIds() {
		return canMoveKnightIntersecIds;
	}

	public Knight getSelectedKnight() {
		return selectedKnight;
	}

	public boolean doShowSelectResourceMenu() {
		return showSelectResourceMenu;
	}

	public String getShowSelectResourceMenuReason() {
		return showSelectResourceReason;
	}

	public void setShowSelectResourceMenu(boolean b) {
		showSelectResourceMenu = b;
	}

	public void setShowSelectResourceMenuReason(String reason) {
		this.showSelectResourceReason = reason;
	}

	public boolean doShowSelectPlayerMenu() {
		return showSelectPlayerMenu;
	}

	public void setShowSelectPlayerMenu(boolean b) {
		this.showSelectPlayerMenu = b;
	}

	public boolean getAttacked() {
		return attacked;
	}

	public void setAttacked(boolean b) {
		attacked = b;
	}

	public int getTradeImprovementLevel() {
		return tradeImprovementLevel;
	}

	public void setTradeImprovementLevel(int value) {
		this.tradeImprovementLevel = value;
	}

	public int getPoliticsImprovementLevel() {
		return politicsImprovementLevel;
	}

	public void setPoliticsImprovementLevel(int value) {
		this.politicsImprovementLevel = value;
	}

	public int getScienceImprovementLevel() {
		return scienceImprovementLevel;
	}

	public void setScienceImprovementLevel(int value) {
		this.scienceImprovementLevel = value;
	}

	public boolean doShowProgressCardMenu() {
		return showProgressCardMenu;
	}

	public void setShowProgressCardMenu(boolean b) {
		this.showProgressCardMenu = b;
	}

	public boolean doShowSelectCardTypeMenu() {
		return showSelectCardTypeMenu;
	}

	public void setShowSelectCardTypeMenu(boolean b) {
		this.showSelectCardTypeMenu = b;
	}

	public int getBarbarianCounter() {
		return barbarianCounter;
	}

	public void setBarbarianCounter(int i) {
		barbarianCounter = i;
	}

	public void setSelectResourceMessage(String string) {
		this.selectResourceMsg = string;
	}

	public String getSelectResourceMessage() {
		return selectResourceMsg;
	}

	public boolean doShowSelectCommodityMenu() {
		return showSelectCommodityMenu;
	}

	public void setShowSelectCommodityMenu(boolean b) {
		this.showSelectCommodityMenu = b;
	}

	public String getSelectCommodityMessage() {
		return selectCommodityMsg;
	}

	public void setSelectCommodityMessage(String string) {
		this.selectCommodityMsg = string;
	}

	public void setPlayersToShow(ArrayList<String> playersWithMoreVPs) {
		this.playersToShow = playersWithMoreVPs;

	}

	public ArrayList<String> getPlayersToShow() {
		ArrayList<String> ret = new ArrayList<>();
		ret.addAll(playersToShow);
		return ret;
	}

	public void doShowSelectHexLayer(boolean b) {
		doShowSelectHexLayer = b;
	}

	public boolean getSelectHexLayer() {
		return doShowSelectHexLayer;
	}

	/**
	 * Set the resource for which the player has a merchant fleet advantage. Set
	 * it to null to remove the advantage.
	 */
	public void setMerchantFleetAdvantage(ArrayList<ResourceType> resourcesWithAdvantage) {
		this.merchantFleetAdvantage = resourcesWithAdvantage;

	}

	public ArrayList<ResourceType> getMerchantFleetAdvantage() {
		return merchantFleetAdvantage;
	}
	
	public int getVictoryPoint() {
		return victoryPoints;
	}
	
	public void setVictoryPoints(int value){
		victoryPoints = value;
	}
}
